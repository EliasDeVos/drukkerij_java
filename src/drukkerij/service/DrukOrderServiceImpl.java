package drukkerij.service;


import drukkerij.dao.DrukOrderDAO;
import drukkerij.dao.DrukOrderDAOImpl;
import drukkerij.model.DrukOrder;

import java.util.List;

/**
 * Created by Pol on 23/02/2015.
 */
public class DrukOrderServiceImpl implements DrukOrderService {
    private DrukOrderDAO contactDAO = new DrukOrderDAOImpl();

    @Override
    public void addDrukOrder(DrukOrder drukOrder) {
        contactDAO.addDrukOrder(drukOrder);
    }

    @Override
    public List<DrukOrder> listDrukOrder() {
        return contactDAO.listDrukOrder();
    }

    @Override
    public void removeDrukOrder(Integer id) {
        contactDAO.removeDrukOrder(id);
    }

    @Override
    public void updateDrukOrder(DrukOrder drukOrder) throws Exception{
        contactDAO.updateDrukOrder(drukOrder);
    }
}
