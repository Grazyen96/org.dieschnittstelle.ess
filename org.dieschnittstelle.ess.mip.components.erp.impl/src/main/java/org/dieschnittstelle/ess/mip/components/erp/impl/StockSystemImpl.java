package org.dieschnittstelle.ess.mip.components.erp.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.ess.entities.erp.PointOfSale;
import org.dieschnittstelle.ess.entities.erp.StockItem;
import org.dieschnittstelle.ess.mip.components.erp.api.StockSystem;
import org.dieschnittstelle.ess.mip.components.erp.crud.api.PointOfSaleCRUD;
import org.dieschnittstelle.ess.mip.components.erp.crud.impl.PointOfSaleCRUDImpl;
import org.dieschnittstelle.ess.mip.components.erp.crud.impl.StockItemCRUD;
import org.dieschnittstelle.ess.utils.interceptors.Logged;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
@Logged
public class StockSystemImpl  implements StockSystem {

    @Inject
    private PointOfSaleCRUD posCRUD ;

    @Inject
    private StockItemCRUD stockItemCRUD;

    @Override
    public void addToStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        PointOfSale pos = posCRUD.readPointOfSale(pointOfSaleId);
        StockItem si = stockItemCRUD.readStockItem(product, pos);

        if (si == null) {
            si = new StockItem(product, pos, units);
            stockItemCRUD.createStockItem(si);
        }
        else {
            si.setUnits(si.getUnits() + units);
            stockItemCRUD.updateStockItem(si);
        }

    }

    @Override
    public void removeFromStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        this.addToStock(product, pointOfSaleId, -units);
    }

    @Override
    public List<IndividualisedProductItem> getProductsOnStock(long pointOfSaleId) {
        PointOfSale pos = posCRUD.readPointOfSale(pointOfSaleId);
        List<IndividualisedProductItem> products = new ArrayList<>();
        for (StockItem si : stockItemCRUD.readStockItemsForPointOfSale(pos)) {
            products.add(si.getProduct());
        }
        return products;
    }

    @Override
    public List<IndividualisedProductItem> getAllProductsOnStock() {
        List<IndividualisedProductItem> products = new ArrayList<>();
        for (PointOfSale pos : posCRUD.readAllPointsOfSale()) {
            for (IndividualisedProductItem product : getProductsOnStock(pos.getId())) {
                if (!products.contains(product)) {
                    products.add(product);
                }
            }
        }
        return products;
    }

    @Override
    public int getUnitsOnStock(IndividualisedProductItem product, long pointOfSaleId) {
        PointOfSale pos = posCRUD.readPointOfSale(pointOfSaleId);
        StockItem si = stockItemCRUD.readStockItem(product, pos);
        if (si == null) {
            return 0;
        }
        return si.getUnits();
    }

    @Override
    public int getTotalUnitsOnStock(IndividualisedProductItem product) {
        int total = 0;
        for (StockItem si : stockItemCRUD.readStockItemsForProduct(product)) {
            total += si.getUnits();
        }
        return total;
    }

    @Override
    public List<Long> getPointsOfSale(IndividualisedProductItem product) {
        List<Long> pointsOfSale = new ArrayList<>();
        for (StockItem si : stockItemCRUD.readStockItemsForProduct(product)) {
            if (!pointsOfSale.contains(si.getPos().getId())) {
                pointsOfSale.add(si.getPos().getId());
            }
        }
        return pointsOfSale;
    }
}
