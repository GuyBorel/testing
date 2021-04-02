package de.uniwue.jpp.testing;


import de.uniwue.jpp.testing.interfaces.DayManager;
import de.uniwue.jpp.testing.interfaces.Order;
import de.uniwue.jpp.testing.util.AbstractTestClass;
import de.uniwue.jpp.testing.util.DataGenerator;
import de.uniwue.jpp.testing.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.*;

public class TestSimpleDayManager extends AbstractTestClass <DayManager> {

    @Test
    public void testConstructorValidArguments() {

        LocalDate date1 = LocalDate.of(2022, 10, 4);
        LocalDate date2 = LocalDate.of(2022, 3, 16);
        LocalDate date3 = LocalDate.of(2022, 4, 16);
        LocalDate date4 = LocalDate.of(2022, 5, 16);
        LocalDate date5 = LocalDate.of(2022, 6, 16);

        try {
            construct(date1, 3, 4);
            construct(date2, 4, 5);
            construct(date3, 5, 6);
            construct(date4, 6, 7);
            construct(date5, 7, 8);

        } catch (Exception error) {
            Assertions.fail("Testing valid arguments for DayManager constructor");
        }

    }

    @Test
    public void testConstructorInvalidArguments() {
        LocalDate date2 = LocalDate.of(2022, 3, 16);
        LocalDate date1 = LocalDate.of(2020, 4, 9);

        TestUtils.assertThrowsWithMessage(IllegalArgumentException.class, () -> construct(null, 2, 4), "Date must not be null!", "Testing invalid date argument for DayManager constructor");

        TestUtils.assertThrowsWithMessage(IllegalArgumentException.class, () -> construct(date1, 2, 4), "Date must not be before current date!", "Testing invalid date argument for DayManager constructor");
        TestUtils.assertThrowsWithMessage(IllegalArgumentException.class, () -> construct(date2, 0, 4), "NumberOfTimeSlots must not be zero or negative!", "Testing invalid numberOfTimeSlots argument for DayManager constructor");
        TestUtils.assertThrowsWithMessage(IllegalArgumentException.class, () -> construct(date2, -1, 4), "NumberOfTimeSlots must not be zero or negative!", "Testing invalid numberOfTimeSlots argument for DayManager constructor");
        TestUtils.assertThrowsWithMessage(IllegalArgumentException.class, () -> construct(date2, 2, 0), "CapacityPerSlot must not be zero or negative!", "Testing invalid capacityPerSlot argument for DayManager constructor");
        TestUtils.assertThrowsWithMessage(IllegalArgumentException.class, () -> construct(date2, 2, -1), "CapacityPerSlot must not be zero or negative!", "Testing invalid capacityPerSlot argument for DayManager constructor");


    }

    @Test
    public void testAddOrderInvalidArguments() {


        DayManager dayManager = construct(LocalDate.of(2022, 1, 2), 3, 4);


        Order goodOrder = DataGenerator.createOrderMock(1);



        TestUtils.assertThrowsWithMessage(IllegalArgumentException.class, () -> dayManager.addOrder(null, 1), "Order must not be null!", "Testing invalid order argument for addOrder()");

        TestUtils.assertThrowsWithMessage(IllegalArgumentException.class, () -> dayManager.addOrder(null), "Order must not be null!", "Testing invalid order argument for addOrder()");

        TestUtils.assertThrowsWithMessage(IllegalArgumentException.class, () -> dayManager.addOrder(goodOrder, -1), "Illegal slot index!", "Testing invalid slot argument for addOrder()");

        TestUtils.assertThrowsWithMessage(IllegalArgumentException.class, () -> dayManager.addOrder(goodOrder, 5), "Illegal slot index!", "Testing invalid slot argument for addOrder()");

        dayManager.addOrder(goodOrder);
        TestUtils.assertThrowsWithMessage(IllegalArgumentException.class, () -> dayManager.addOrder(goodOrder, 1), "Illegal order: This guest has already ordered!", "Testing invalid order argument for addOrder()");
        TestUtils.assertThrowsWithMessage(IllegalArgumentException.class, () -> dayManager.addOrder(goodOrder), "Illegal order: This guest has already ordered!", "Testing invalid order argument for addOrder()");
    }

    @Test
    private void helpMethod2(int nos, int cps) {
        DayManager dayManager = construct(LocalDate.of(2022, 2, 3), nos, cps);
        StringBuilder message = new StringBuilder("Executing addOrder sequence on a DayManager with " + nos + " time slots and " + cps + " capacity per slot:\n");

        for(int i = 0; i < nos; i++){
            for(int j = 0; j < cps; j++){
                Order order = DataGenerator.createOrderMock(3);
                Optional <Integer> pref2 = dayManager.addOrder(order);
                message.append("adding Order\n");
                try {
                    Assertions.assertTrue(pref2.isPresent());
                    Assertions.assertEquals(i, pref2.get());
                } catch (AssertionError e){
                    Assertions.fail(message + "Wrong return value.");
                }
            }
        }
        Optional <Integer> pref2 = dayManager.addOrder(DataGenerator.createOrderMock(3));
        try {
            Assertions.assertEquals(Optional.empty(), pref2);
        } catch (AssertionError e){
            Assertions.fail(message + "adding Order\nWrong return value.");
        }
    }

    @Test
    public void testAddOrderWithSlotArgument() {

        //Szenario (1, 1)..................................................................................................
        DayManager dayManager1 = construct(LocalDate.of(2022,2,3),1,1);
        String str1 = "Executing addOrder sequence on a DayManager with 1 time slots and 1 capacity per slot:\n" +
                "adding Order with preferred slot 0\n" +
                "adding Order with preferred slot 0\n" +
                "Wrong return value.";
        Order order1 = DataGenerator.createOrderMock(1);
        Order order2 = DataGenerator.createOrderMock(2);
        Order order3 = DataGenerator.createOrderMock(3);
        Order order4 = DataGenerator.createOrderMock(4);
        Order order5 = DataGenerator.createOrderMock(5);
        Order order6 = DataGenerator.createOrderMock(6);
        Order order7 = DataGenerator.createOrderMock(7);
         if(!dayManager1.addOrder(order1,0).equals(Optional.of(0)))
            Assertions.fail("Wrong return value.");

        if(!dayManager1.addOrder(DataGenerator.createOrderMock(2),0).equals(Optional.empty()))
            Assertions.fail(str1);

        //Szenario (1, 5)..................................................................................................

        DayManager dayManager2 = construct(LocalDate.of(2022,3,3),1,5);
        String str2 ="Executing addOrder sequence on a DayManager with 1 time slots and 5 capacity per slot:\n" +
                "adding Order with preferred slot 0\n" +
                "adding Order with preferred slot 0\n" +
                "adding Order with preferred slot 0\n" +
                "adding Order with preferred slot 0\n" +
                "adding Order with preferred slot 0\n" +
                "adding Order with preferred slot 0\n" +
                "Wrong return value.";

        for (int i = 1; i <= 5; i++) {
            if(!dayManager2.addOrder(DataGenerator.createOrderMock(i),0).equals(Optional.of(0)))
                Assertions.fail("Wrong return value.");
        }

        if(!dayManager2.addOrder(DataGenerator.createOrderMock(6),0).equals(Optional.empty()))
            Assertions.fail(str2);

        //Szenario (5, 4)..................................................................................................

        DayManager dayManager3 = construct(LocalDate.of(2022,4,3), 3,2);
        String str3 = "Executing addOrder sequence on a DayManager with 3 time slots and 2 capacity per slot:\n" +
                "adding Order with preferred slot 1\n" +
                "adding Order with preferred slot 1\n" +
                "adding Order with preferred slot 1\n" +
                "Wrong return value.";

        if(!dayManager3.addOrder(order1,1).equals(Optional.of(1)))
            Assertions.fail(str3);
        if(!dayManager3.addOrder(order2,1).equals(Optional.of(1)))
            Assertions.fail(str3);
        if(!dayManager3.addOrder(order3,1).equals(Optional.of(0)))
            Assertions.fail(str3);
        if(!dayManager3.addOrder(order4,1).equals(Optional.of(0)))
            Assertions.fail(str3);
        if(!dayManager3.addOrder(order5,1).equals(Optional.of(2)))
            Assertions.fail(str3);
        if(!dayManager3.addOrder(order6,1).equals(Optional.of(2)))
            Assertions.fail(str3);
        if(!dayManager3.addOrder(order7,0).equals(Optional.empty()))
            Assertions.fail("Daymanager is full!");


        //Szenario (3, 2)..................................................................................................

        DayManager dayManager4 = construct(LocalDate.of(2022,5,3),5,4);
        String str4 = "Executing addOrder sequence on a DayManager with 5 time slots and 4 capacity per slot:\n" +
                "adding Order with preferred slot 2\n" +
                "adding Order with preferred slot 2\n" +
                "adding Order with preferred slot 2\n" +
                "adding Order with preferred slot 2\n" +
                "adding Order with preferred slot 2\n" +
                "Wrong return value.";

        for (int i = 1; i <= 4; i++) {
            if(!dayManager4.addOrder(DataGenerator.createOrderMock(i),2).equals(Optional.of(2)))
                Assertions.fail(str4);
        }
        for (int i = 5; i <= 8; i++) {
            if(!dayManager4.addOrder(DataGenerator.createOrderMock(i),2).equals(Optional.of(1)))
                Assertions.fail(str4);
        }
        for (int i = 9; i <= 12; i++) {
            if(!dayManager4.addOrder(DataGenerator.createOrderMock(i),2).equals(Optional.of(3)))
                Assertions.fail(str4);
        }
        for (int i = 13; i <= 16; i++) {
            if(!dayManager4.addOrder(DataGenerator.createOrderMock(i),2).equals(Optional.of(0)))
                Assertions.fail(str4);
        }
        for (int i = 17; i <= 20; i++) {
            if(!dayManager4.addOrder(DataGenerator.createOrderMock(i),2).equals(Optional.of(4)))
                Assertions.fail(str4);
        }

        if(!dayManager4.addOrder(DataGenerator.createOrderMock(21),0).equals(Optional.empty()))
            Assertions.fail("Daymanager is full!");

    }

    @Test
    public void testAddOrderWithoutSlotArgument() {
        //Szenario (1, 1)..................................................................................................
        helpMethod2(1,1);
        //Szenario (3, 2)..................................................................................................
        helpMethod2(3,2);

    }

    @Test
    private void helpMethod3(int nos, int cps) {
        DayManager dayManager = construct(LocalDate.of(2022, 2, 3), nos, cps);

        StringBuilder message = new StringBuilder("Executing addOrder sequence on a DayManager with " + nos + " time slots and " + cps + " capacity per slot:\n");

        Map <Integer, List <Order>> orderIntegerMap = new HashMap <>();
        for (int i = 0; i < nos; i++) {
            orderIntegerMap.put(i, new ArrayList <>());
        }
        for(int i = 0; i < nos; i++){
            for(int j = 0; j < cps; j++){
                Order order = DataGenerator.createOrderMock(3);
                orderIntegerMap.get(i).add(order);
                Optional <Integer> pref2 = dayManager.addOrder(order, i);
                message.append("adding Order with preferred slot ").append(i).append("\n");
                for (int k = 0; k < i; k++) {
                    Collection <Order> orders = dayManager.getOrdersForSlot(k);
                    TestUtils.assertCollectionsEquals(orderIntegerMap.get(k), orders, message + "Incorrect collection returned by getOrdersForSlot() for slot " + k);
                    TestUtils.assertCollectionIsUnmodifiable(orders, "Collection returned by getOrdersForSlot() is modifiable");
                }
                TestUtils.assertCollectionsEquals(orderIntegerMap.get(i), dayManager.getOrdersForSlot(i), message + "Incorrect collection returned by getOrdersForSlot() for slot " + i);
                TestUtils.assertCollectionIsUnmodifiable(dayManager.getOrdersForSlot(i), "Collection returned by getOrdersForSlot() is modifiable");
                for (int k = i + 1; k < nos; k++) {
                    Collection <Order> orders = dayManager.getOrdersForSlot(k);
                    TestUtils.assertCollectionsEquals(Collections.emptyList(), orders, message + "Incorrect collection returned by getOrdersForSlot() for slot " + k);
                    TestUtils.assertCollectionIsUnmodifiable(orders, "Collection returned by getOrdersForSlot() is modifiable");
                }

            }
        }
        Optional <Integer> pref2 = dayManager.addOrder(DataGenerator.createOrderMock(3));
        for (int k = 0; k < nos; k++) {
            Collection <Order> orders = dayManager.getOrdersForSlot(k);
            TestUtils.assertCollectionsEquals(orderIntegerMap.get(k), orders, message + "Incorrect collection returned by getOrdersForSlot() for slot " + k);
            TestUtils.assertCollectionIsUnmodifiable(orders, "Collection returned by getOrdersForSlot() is modifiable");
        }
    }

    @Test
    private void helpMethod4(int nos, int cps) {
        DayManager dayManager = construct(LocalDate.of(2022, 2, 3), nos, cps);

        StringBuilder message = new StringBuilder("Executing addOrder sequence on a DayManager with " + nos + " time slots and " + cps + " capacity per slot:\n");

        Map <Integer, List <Order>> orderIntegerMap = new HashMap <>();
        for (int i = 0; i < nos; i++) {
            orderIntegerMap.put(i, new ArrayList <>());
        }
        for(int i = 0; i < nos; i++){
            for(int j = 0; j < cps; j++){
                Order order = DataGenerator.createOrderMock(3);
                orderIntegerMap.get(i).add(order);
                Optional <Integer> pref2 = dayManager.addOrder(order);
                message.append("adding Order\n");
                for (int k = 0; k < i; k++) {
                    Collection <Order> orders = dayManager.getOrdersForSlot(k);
                    TestUtils.assertCollectionsEquals(orderIntegerMap.get(k), orders, message + "Incorrect collection returned by getOrdersForSlot() for slot " + k);
                    TestUtils.assertCollectionIsUnmodifiable(orders, "Collection returned by getOrdersForSlot() is modifiable");
                }
                TestUtils.assertCollectionsEquals(orderIntegerMap.get(i), dayManager.getOrdersForSlot(i), message + "Incorrect collection returned by getOrdersForSlot() for slot " + i);
                TestUtils.assertCollectionIsUnmodifiable(dayManager.getOrdersForSlot(i), "Collection returned by getOrdersForSlot() is modifiable");
                for (int k = i + 1; k < nos; k++) {
                    Collection <Order> orders = dayManager.getOrdersForSlot(k);
                    TestUtils.assertCollectionsEquals(Collections.emptyList(), orders, message + "Incorrect collection returned by getOrdersForSlot() for slot " + k);
                    TestUtils.assertCollectionIsUnmodifiable(orders, "Collection returned by getOrdersForSlot() is modifiable");
                }

            }
        }
        Optional <Integer> pref2 = dayManager.addOrder(DataGenerator.createOrderMock(3));
        for (int k = 0; k < nos; k++) {
            Collection <Order> orders = dayManager.getOrdersForSlot(k);
            TestUtils.assertCollectionsEquals(orderIntegerMap.get(k), orders, message + "Incorrect collection returned by getOrdersForSlot() for slot " + k);
            TestUtils.assertCollectionIsUnmodifiable(orders, "Collection returned by getOrdersForSlot() is modifiable");
        }
    }

    @Test
    public void testGetOrdersForSlot() {
        //Szenario (1, 1)..................................................................................................
        helpMethod3(1, 1);

        //Szenario (1, 5)..................................................................................................
        helpMethod3(1, 5);

        //Szenario (5, 4)..................................................................................................
        helpMethod3(5, 4);

        //Szenario (3, 2)..................................................................................................
        helpMethod3(3,2);

        //Szenario (1, 1)..................................................................................................
        helpMethod4(1,1);

        //Szenario (3, 2)..................................................................................................
        helpMethod4(3,2);
    }
    @Test
    private void helpMethod5(int nos, int cps) {
        DayManager dayManager = construct(LocalDate.of(2022, 2, 3), nos, cps);

        StringBuilder message = new StringBuilder("Executing addOrder sequence on a DayManager with " + nos + " time slots and " + cps + " capacity per slot:\n");

        List<Order> orderInteger = new ArrayList<>();
        for(int i = 0; i < nos; i++){
            for(int j = 0; j < cps; j++){
                Order order = DataGenerator.createOrderMock(3);
                orderInteger.add(order);
                Optional <Integer> pref2 = dayManager.addOrder(order, i);
                message.append("adding Order with preferred slot ").append(i).append("\n");

                TestUtils.assertCollectionsEquals(orderInteger, dayManager.getAllOrders(), message + "Incorrect collection returned by getAllOrders()");
                TestUtils.assertCollectionIsUnmodifiable(dayManager.getAllOrders(), "Collection returned by getAllOrders() is modifiable");
            }
        }

        Optional <Integer> pref2 = dayManager.addOrder(DataGenerator.createOrderMock(3));
        TestUtils.assertCollectionsEquals(orderInteger, dayManager.getAllOrders(), message + "Incorrect collection returned by getAllOrders()");
        TestUtils.assertCollectionIsUnmodifiable(dayManager.getAllOrders(), "Collection returned by getAllOrders() is modifiable");
    }

    @Test
    private void helpMethod6(int nos, int cps) {
        DayManager dayManager = construct(LocalDate.of(2022, 2, 3), nos, cps);

        StringBuilder message = new StringBuilder("Executing addOrder sequence on a DayManager with " + nos + " time slots and " + cps + " capacity per slot:\n");

        List<Order> orderInteger = new ArrayList<>();
        for(int i = 0; i < nos; i++){
            for(int j = 0; j < cps; j++){
                Order order = DataGenerator.createOrderMock(3);
                orderInteger.add(order);
                Optional <Integer> pref2 = dayManager.addOrder(order, i);
                message.append("adding Order with preferred slot\n");

                TestUtils.assertCollectionsEquals(orderInteger, dayManager.getAllOrders(), message + "Incorrect collection returned by getAllOrders()");
                TestUtils.assertCollectionIsUnmodifiable(dayManager.getAllOrders(), "Collection returned by getAllOrders() is modifiable");
            }
        }

        Optional <Integer> pref2 = dayManager.addOrder(DataGenerator.createOrderMock(3));
        TestUtils.assertCollectionsEquals(orderInteger, dayManager.getAllOrders(), message + "Incorrect collection returned by getAllOrders()");
        TestUtils.assertCollectionIsUnmodifiable(dayManager.getAllOrders(), "Collection returned by getAllOrders() is modifiable");
    }
    @Test
    public void testGetAllOrders() {
        //Szenario (1, 1)..................................................................................................
        helpMethod5(1, 1);

        //Szenario (1, 5)..................................................................................................
        helpMethod5(1, 5);

        //Szenario (5, 4)..................................................................................................
        helpMethod5(5, 4);

        //Szenario (3, 2)..................................................................................................
        helpMethod5(3,2);

        //Szenario (1, 1)..................................................................................................
        helpMethod6(1,1);

        //Szenario (3, 2)..................................................................................................
        helpMethod6(3,2);
    }

    @Test
    public void testCalculateEarnings() {
        DayManager dayManager = construct(LocalDate.of(2022, 1, 2), 3, 4);

        Order order1 = DataGenerator.createOrderMock(1);
        Order order2 = DataGenerator.createOrderMock(2);
        Order order3 = DataGenerator.createOrderMock(3);
        Order order4 = DataGenerator.createOrderMock(4);
        Order order5 = DataGenerator.createOrderMock(5);

        Assertions.assertEquals(0.0, dayManager.calculateEarnings());

        dayManager.addOrder(order1);
        Assertions.assertEquals(0.5, dayManager.calculateEarnings());
        dayManager.addOrder(order2);
        Assertions.assertEquals(2.0, dayManager.calculateEarnings());
        dayManager.addOrder(order3);
        Assertions.assertEquals(5.0, dayManager.calculateEarnings());
        dayManager.addOrder(order4);
        Assertions.assertEquals(10.0, dayManager.calculateEarnings());
        dayManager.addOrder(order5);

        Assertions.assertEquals(17.5, dayManager.calculateEarnings());
    }

    @Test
    public void testToString() {


        DayManager dayManager1 = construct(LocalDate.of(2022, 1, 2), 3, 4);
        DayManager dayManager2 = construct(LocalDate.of(2022, 2, 3), 4, 5);
        DayManager dayManager3 = construct(LocalDate.of(2022, 3, 4), 5, 6);
        DayManager dayManager4 = construct(LocalDate.of(2022, 4, 5), 6, 7);
        DayManager dayManager5 = construct(LocalDate.of(2022, 5, 6), 7, 8);


        Assertions.assertEquals("SimpleDayManager (02.01.22)", dayManager1.toString(), "Incorrect string representation returned by toString()");
        Assertions.assertEquals("SimpleDayManager (03.02.22)", dayManager2.toString(), "Incorrect string representation returned by toString()");
        Assertions.assertEquals("SimpleDayManager (04.03.22)", dayManager3.toString(), "Incorrect string representation returned by toString()");
        Assertions.assertEquals("SimpleDayManager (05.04.22)", dayManager4.toString(), "Incorrect string representation returned by toString()");
        Assertions.assertEquals("SimpleDayManager (06.05.22)", dayManager5.toString(), "Incorrect string representation returned by toString()");
    }
}
