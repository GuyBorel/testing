package de.uniwue.jpp.testing;

import de.uniwue.jpp.testing.enums.GuestType;
import de.uniwue.jpp.testing.interfaces.Guest;
import de.uniwue.jpp.testing.util.AbstractTestClass;
import de.uniwue.jpp.testing.util.DataGenerator;
import de.uniwue.jpp.testing.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestGuest extends AbstractTestClass <Guest> {


    @Test
    public void testConstructorValidArguments() {
        List <String> guestList = DataGenerator.createGuestNames(5);
        List <GuestType> guestTypes = DataGenerator.createGuestTypes(2, 1, 2);
        int count = 0;
        for (String value : guestList) {
            try {
                construct(value, guestTypes.get(count));
                count++;
            } catch (Exception e) {
                fail("Testing valid arguments for Guest constructor");
            }
        }

    }

    @Test
    public void testConstructorInvalidArguments() {

        List <String> value = DataGenerator.createGuestNames(5);

        for (String s : value) {
            TestUtils.assertThrowsWithMessage(IllegalArgumentException.class, () -> construct(s, null), "GuestType must not be null!", "Testing invalid guestType argument for Guest constructor");
            TestUtils.assertThrowsWithMessage(IllegalArgumentException.class, () -> construct(null, GuestType.GUEST), "Name must not be null!", "Testing invalid name argument for Guest constructor");
            TestUtils.assertThrowsWithMessage(IllegalArgumentException.class, () -> construct("", GuestType.STAFF), "Name must not be empty!", "Testing invalid name argument for Guest constructor");
        }
    }


    @Test
    public void testGetName() {

        List <String> stringList = DataGenerator.createGuestNames(5);
        List <GuestType> guestList = DataGenerator.createGuestTypes(2, 1, 2);
        List <Guest> list = new ArrayList <>();

        for (int i = 0; i < 5; i++) {
            Guest guest = construct(stringList.get(i), guestList.get(i));
            list.add(guest);
        }
        int i = 0;
        for (Guest guest : list) {
            try {
                Assertions.assertEquals(guest.getName(), stringList.get(i));
                i++;
            } catch (AssertionError e) {
                fail("Incorrect name returned by getName()");
            }
        }

    }

    @Test
    public void testGetGuestType() {

        List <String> stringList = DataGenerator.createGuestNames(5);
        List <GuestType> guestList = DataGenerator.createGuestTypes(2, 1, 2);
        List <Guest> list = new ArrayList <>();
        System.out.println(guestList.toString());
        System.out.println();


        for (int i = 0; i < 5; i++) {
            Guest guest = construct(stringList.get(i), guestList.get(i));
            list.add(guest);
            System.out.println(list.toString());
        }
        int k = 0;
        for (Guest guest : list) {
            try {
                Assertions.assertEquals(guest.getGuestType(), guestList.get(k));
                k++;
            } catch (AssertionError error) {
                fail("Incorrect guestType returned by getGuestType()");
                error.getClass().getEnumConstants();
            }
        }
    }

    @Test
    public void testEquals() {
        List <String> guestList = DataGenerator.createGuestNames(5);
        List <GuestType> guestTypes = DataGenerator.createGuestTypes(2, 1, 2);
      /*  List <Guest> list = new ArrayList <>();

        for (int i = 0; i < 5; i++) {
            Guest guest = construct(guestList.get(i), guestTypes.get(i));
            list.add(guest);
            System.out.println(list.toString());
        }*/
        int j = 4;
        for (int i = 0; i < 5; i++) {
            try {

                assertEquals(construct(guestList.get(i), guestTypes.get(i)), construct(guestList.get(i), guestTypes.get(j)));
                j--;
            } catch (AssertionError e) {
                fail("Two guests with identical names should be equal");
            }
        }

        int k = 0;
        try {
            Assertions.assertNotEquals(construct("Dish", guestTypes.get(k)), construct("Guest1", guestTypes.get(k)));
        } catch (AssertionError error) {
            fail("Two guests with different names should not be equal");
        }

    }


    @Test
    public void testHashCode() {
        List <String> guestList = DataGenerator.createGuestNames(9);
        List <GuestType> guestTypes = DataGenerator.createGuestTypes(3, 3, 3);
        List <Guest> guestList1 = new ArrayList <>();
        List <Guest> guestList2 = new ArrayList <>();

        for (int i = 0; i < 9; i++) {
            guestList1.add(construct(guestList.get(i), guestTypes.get(i)));
        }

        for (int i = 0; i < 9; i++) {
            guestList2.add(construct(guestList.get(i), guestTypes.get(((guestTypes.size() - 1) - i))));
        }

        for (int i = 0; i < 9; i++) {
            if (!(guestList1.get(i).hashCode() == guestList2.get(i).hashCode())) {
                throw new AssertionError("Two guests with identical names should have the same hash code");
            }
        }
    }

    @Test
    public void testToString() {
        List <String> guestList = DataGenerator.createGuestNames(9);
        List <GuestType> guestTypes = DataGenerator.createGuestTypes(3, 3, 3);
        List <Guest> list = new ArrayList <>();

        for (int i = 0; i < 9; i++) {
            Guest guest = construct(guestList.get(i), guestTypes.get(i));
            list.add(guest);
            System.out.println(list.toString());
        }

        for (Guest guest : list) {
            if (!guest.toString().equals(guest.getName() + " (" + guest.getGuestType() + ")")) {
                throw new AssertionError("Incorrect string representation returned by toString()");
            }
        }
    }
}

