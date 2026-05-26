package org.dieschnittstelle.ess.jrs;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.entities.GenericCRUDExecutor;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;

import java.util.List;

/*
 * DONE JRS2: implementieren Sie hier die im Interface deklarierten Methoden
 */

public class ProductCRUDServiceImpl implements IProductCRUDService {

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(TouchpointCRUDServiceImpl.class);


  private GenericCRUDExecutor<IndividualisedProductItem> productCRUD;

	public ProductCRUDServiceImpl(@Context ServletContext servletContext, @Context HttpServletRequest request) {
		logger.info("<constructor>: " + servletContext + "/" + request);
		// read out the dataAccessor
		this.productCRUD = (GenericCRUDExecutor<IndividualisedProductItem>) servletContext.getAttribute("productCRUD");

		logger.debug("read out the productCRUD from the servlet context: " + this.productCRUD);
	}

	@Override
	public IndividualisedProductItem createProduct(
			IndividualisedProductItem prod) {
		return this.productCRUD.createObject(prod);
	}

	@Override
	public List<IndividualisedProductItem> readAllProducts() {

		return (List) this.productCRUD.readAllObjects();
	}

	@Override
	public IndividualisedProductItem updateProduct(long id,
			IndividualisedProductItem update) {
		return this.productCRUD.updateObject(update);
	}

	@Override
	public boolean deleteProduct(long id) {
		return this.productCRUD.deleteObject(id);
	}

	@Override
	public IndividualisedProductItem readProduct(long id) {

		IndividualisedProductItem product = this.productCRUD.readObject(id);
		if (product == null) {
			throw new jakarta.ws.rs.NotFoundException("Product with id " + id + " not found");
		}
		return product;
	}
	
}
