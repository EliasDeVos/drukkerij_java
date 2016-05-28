package drukkerij.service;


import drukkerij.dao.DrukItemDAO;
import drukkerij.dao.DrukItemDAOImpl;
import drukkerij.model.DrukItem;

import java.util.List;

/**
 * Created by Pol on 23/02/2015.
 */
public class DrukItemServiceImpl implements DrukItemService {
    private DrukItemDAO contactDAO = new DrukItemDAOImpl();

    @Override
    public void addDrukOrder(DrukItem drukItem) {
        contactDAO.addDrukOrder(drukItem);
    }

    @Override
    public List<DrukItem> listDrukOrder() {
        return contactDAO.listDrukOrder();
    }

    @Override
    public void removeDrukOrder(Integer id) {
        contactDAO.removeDrukOrder(id);
    }

    @Override
    public void updateDrukOrder(DrukItem drukItem) throws Exception {
        contactDAO.updateDrukOrder(drukItem);
    }

    @Override
    public DrukItem getDrukItem(Integer drukItemId) {
        return contactDAO.getDrukItem(drukItemId);
    }

}
