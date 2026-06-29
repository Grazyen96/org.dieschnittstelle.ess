package org.dieschnittstelle.ess.mip.components.erp.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.ess.entities.erp.PointOfSale;
import org.dieschnittstelle.ess.entities.erp.StockItem;
import org.dieschnittstelle.ess.mip.components.erp.crud.impl.EntityManagerProvider;
import org.dieschnittstelle.ess.mip.components.erp.crud.impl.StockItemCRUD;
import org.dieschnittstelle.ess.utils.interceptors.Logged;

import java.util.List;

@ApplicationScoped
@Transactional
@Logged
public class StockItemCRUDImpl  implements StockItemCRUD {

    @Inject
    @EntityManagerProvider.ERPDataAccessor
    private EntityManager em;

    @Override
    public StockItem createStockItem(StockItem item) {
        em.persist(item);
        return item;
    }

    @Override
    public StockItem readStockItem(IndividualisedProductItem prod, PointOfSale pos) {
        Query query = em.createQuery("SELECT DISTINCT si FROM StockItem si WHERE si.product.id = :prodId AND si.pos.id = :posId");
        query.setParameter("prodId", prod.getId());
        query.setParameter("posId", pos.getId());

        List<StockItem> sis = query.getResultList();

        if(!sis.isEmpty())
            return sis.get(0);
        else
            return null;
    }

    @Override
    public StockItem updateStockItem(StockItem item) {
        return null;
    }

    @Override
    public List<StockItem> readStockItemsForProduct(IndividualisedProductItem prod) {
        return List.of();
    }

    @Override
    public List<StockItem> readStockItemsForPointOfSale(PointOfSale pos) {
        return List.of();
    }
}
