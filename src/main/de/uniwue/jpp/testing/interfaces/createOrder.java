package de.uniwue.jpp.testing.interfaces;

import de.uniwue.jpp.testing.enums.DishType;
import de.uniwue.jpp.testing.enums.GuestType;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class createOrder implements Order{

    private final Guest guest;
    private final List <Dish> dishes;

    public createOrder(Guest guest, List<Dish> dishes){

        if (guest == null) {
            throw new IllegalArgumentException("Guest must not be null!");
        }

        if (dishes == null) {
            throw new IllegalArgumentException("Dish collection must not be null!");
        }

        if (dishes.isEmpty()) {
            throw new IllegalArgumentException("Dish collection must not be empty!");
        }
        this.guest = guest;
        this.dishes = dishes;
    }

    @Override
    public double calculatePrice() {
        double value = 0.0;
        for (Dish dish : dishes) {
            value += dish.getBasePrice() * guest.getGuestType().getDiscountFactor();
        }

        boolean Starter = false;
        boolean Main_dish = false;
        boolean Dessert = false;

        for (Dish dish: dishes) {
            if (dish.getDishType().equals(DishType.STARTER)){
                Starter = true;
            } else if (dish.getDishType().equals(DishType.DESSERT)){
                Dessert = true;
            } else if (dish.getDishType().equals(DishType.MAIN_DISH)){
                Main_dish = true;
            }

        }
        if (Starter & Main_dish & Dessert){
            value  = value * 0.8;
        }

        value = Math.round(100.0 * value ) / 100.0;
        return value;
    }

    @Override
    public List <Dish> getDishes() {
        return Collections.unmodifiableList(dishes);
    }

    @Override
    public Guest getGuest() {
        return guest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof createOrder)) return false;

        createOrder that = (createOrder) o;

        if (!guest.equals(that.guest)) return false;
        return dishes.containsAll(that.dishes) && that.dishes.containsAll(dishes);
    }

    @Override
    public int hashCode() {
        int result;
        result = getGuest().hashCode();

        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Order:").append("\n");

        for (Dish dish : dishes) {
            stringBuilder.append("\t").append("- ").append(dish.getName()).append("\n");
        }
        stringBuilder.append("\n");

        stringBuilder.append("\t").append("Name: ").append(guest.getName()).append(" | Total price: ").append(calculatePrice()).append("€");

        return stringBuilder.toString();
    }

    public static void main(String[] args) {

        List<Dish> dishList = new LinkedList <>();
        Guest guest = Guest.createGuest("paul", GuestType.GUEST);
        dishList.add(Dish.createDish("test", 0.5, DishType.MAIN_DISH));
        dishList.add(Dish.createDish("test", 1.0, DishType.DESSERT));
        dishList.add(Dish.createDish("test", 1.5, DishType.DESSERT));
        dishList.add(Dish.createDish("test", 2.0, DishType.OTHER));
        dishList.add(Dish.createDish("test", 2.5, DishType.OTHER));


        System.out.println();
        for (Dish dish : dishList) {
            System.out.println(dish);
        }

        Order order = Order.createOrder(guest,dishList);

        System.out.println("value " + order.calculatePrice());

        System.out.println(Order.createOrder(guest,dishList));

        System.out.println(guest.toString());
    }
}
