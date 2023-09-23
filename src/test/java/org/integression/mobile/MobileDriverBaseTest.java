package org.integression.mobile;


import org.base.mobile.MobileDriverProvider;
import org.integression.mobile.myDish.viewModel.MyDishAllDishesViewModel;

public class MobileDriverBaseTest {

    public MyDishAllDishesViewModel myDishAllDishesViewModel;

    public MobileDriverBaseTest(MobileDriverProvider driverManager) {
        this.myDishAllDishesViewModel = new MyDishAllDishesViewModel(driverManager);
    }
}
