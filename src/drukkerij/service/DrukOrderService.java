package drukkerij.service;


import drukkerij.model.DrukOrder;

import java.util.List;

/**
 * Created by Pol on 23/02/2015.
 */
public interface DrukOrderService {
    public void addDrukOrder(DrukOrder drukOrder);

    public List<DrukOrder> listDrukOrder();

    public void removeDrukOrder(Integer id);

    public void updateDrukOrder(DrukOrder drukOrder);

}
