package org.dieschnittstelle.ess.mip.client.apiclients;

import java.util.List;

import org.dieschnittstelle.ess.entities.erp.Campaign;
import org.dieschnittstelle.ess.mip.components.erp.crud.api.ProductCRUD;
import org.dieschnittstelle.ess.entities.erp.AbstractProduct;

public class ProductCRUDClient implements ProductCRUD {

	private ProductCRUD serviceProxy;

	public ProductCRUDClient() throws Exception {
		// obtain a Resteasy proxy for the ProductCRUD service interface; all methods delegate to it
		this.serviceProxy = ServiceProxyFactory.getInstance().getProxy(ProductCRUD.class);
	}

	public AbstractProduct createProduct(AbstractProduct prod) {
		AbstractProduct created = serviceProxy.createProduct(prod);
		// as a side-effect we set the id of the created product on the argument before returning
		prod.setId(created.getId());
		return created;
	}

	public List<AbstractProduct> readAllProducts() {
		return serviceProxy.readAllProducts();
	}

	public AbstractProduct updateProduct(AbstractProduct update) {
		return serviceProxy.updateProduct(update);
	}

	public AbstractProduct readProduct(long productID) {
		return serviceProxy.readProduct(productID);
	}

	public boolean deleteProduct(long productID) {
		return serviceProxy.deleteProduct(productID);
	}

	@Override
	public List<Campaign> getCampaignsForProduct(long productID) {
		return serviceProxy.getCampaignsForProduct(productID);
	}

}
