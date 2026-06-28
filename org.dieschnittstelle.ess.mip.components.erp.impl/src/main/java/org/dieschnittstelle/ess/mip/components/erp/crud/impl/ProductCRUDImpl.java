package org.dieschnittstelle.ess.mip.components.erp.crud.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.dieschnittstelle.ess.entities.erp.AbstractProduct;
import org.dieschnittstelle.ess.entities.erp.Campaign;
import org.dieschnittstelle.ess.mip.components.erp.crud.api.ProductCRUD;
import org.dieschnittstelle.ess.utils.interceptors.Logged;

import java.util.List;

@ApplicationScoped
@Transactional
@Logged
public class ProductCRUDImpl  implements ProductCRUD {

    @Inject
    @EntityManagerProvider.ERPDataAccessor
    private EntityManager em;

    @Override
    public AbstractProduct createProduct(AbstractProduct prod) {
        em.persist(prod);
        return prod;
    }

    @Override
    public List<AbstractProduct> readAllProducts() {
        // read back the whole AbstractProduct hierarchy (IndividualisedProductItem + Campaign)
        Query query = em.createQuery("SELECT DISTINCT p FROM AbstractProduct p");
        return query.getResultList();
    }

    @Override
    public AbstractProduct updateProduct(AbstractProduct update) {
        // merge returns the managed instance reflecting the update
        return em.merge(update);
    }

    @Override
    public AbstractProduct readProduct(long productID) {
        // find returns null if no entity with the given id exists
        return em.find(AbstractProduct.class, productID);
    }

    @Override
    public boolean deleteProduct(long productID) {
        AbstractProduct prod = em.find(AbstractProduct.class, productID);
        if (prod == null) {
            return false;
        }
        em.remove(prod);
        return true;
    }

    @Override
    public List<Campaign> getCampaignsForProduct(long productID) {
        // JPQL JOIN over the campaign's bundles to find all campaigns that bundle
        // the given product (see CustomerTransactionCRUDImpl.readAllTransactionsForProduct)
        Query query = em.createQuery(
                "SELECT DISTINCT c FROM Campaign c JOIN c.bundles b WHERE b.product.id = :productID");
        query.setParameter("productID", productID);
        return query.getResultList();
    }
}
