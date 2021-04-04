import org.junit.jupiter.api.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


class RestaurantServiceTest {

    RestaurantService service;
    Restaurant restaurant;

    @BeforeEach
    public void initialise_static_restaurant(){
        service = new RestaurantService();
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant = service.addRestaurant("Amelie's cafe","Chennai",openingTime,closingTime);
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);
    }

    @Test
    public void searching_for_existing_restaurant_should_return_expected_restaurant_object() throws restaurantNotFoundException {
        Restaurant searchedRestaurant = service.findRestaurantByName(restaurant.getName());

        assertNotNull(searchedRestaurant);
        assertEquals(restaurant.getName(), searchedRestaurant.getName());
        assertEquals(restaurant.getLocation(), searchedRestaurant.getLocation());
        assertEquals(restaurant.openingTime, searchedRestaurant.openingTime);
        assertEquals(restaurant.closingTime, searchedRestaurant.closingTime);

        List<Item> expected_menu = searchedRestaurant.getMenu();
        List<Item> actualMenu = new ArrayList<Item>();
        Item item1 = new Item("Sweet corn soup",119);
        Item item2 = new Item("Vegetable lasagne", 269);
        actualMenu.add(item1);
        actualMenu.add(item2);

        assertThat(actualMenu.size(), is(expected_menu.size()));
        assertThat(actualMenu, hasItems(item1));
        assertThat(actualMenu, hasItems(item2));
    }

    @Test
    public void searching_for_non_existing_restaurant_should_throw_exception() throws restaurantNotFoundException {
        assertThrows(restaurantNotFoundException.class, () ->{
            service.findRestaurantByName("Restaurant X");
        });
    }

    @Test
    public void remove_restaurant_should_reduce_list_of_restaurants_size_by_1() throws restaurantNotFoundException {
        int initialNumberOfRestaurants = service.getRestaurants().size();
        service.removeRestaurant("Amelie's cafe");
        assertEquals(initialNumberOfRestaurants-1, service.getRestaurants().size());
    }

    @Test
    public void removing_restaurant_that_does_not_exist_should_throw_exception() throws restaurantNotFoundException {
        assertThrows(restaurantNotFoundException.class,()->service.removeRestaurant("Pantry d'or"));
    }

    @Test
    public void add_restaurant_should_increase_list_of_restaurants_size_by_1(){
        int initialNumberOfRestaurants = service.getRestaurants().size();
        service.addRestaurant("Pumpkin Tales","Chennai",LocalTime.parse("12:00:00"),LocalTime.parse("23:00:00"));
        assertEquals(initialNumberOfRestaurants + 1,service.getRestaurants().size());
    }
}