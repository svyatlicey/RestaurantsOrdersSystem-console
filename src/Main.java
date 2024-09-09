//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import org.w3c.dom.ls.LSOutput;

import java.util.*;

public class Main{
    public static void main(String[] args) {
        int result =2;
        HashMap<String, Restaurant> restaurants = new HashMap<>();
        Scanner input = new Scanner(System.in);
        ArrayList<Order> orders = new ArrayList<>();
        ArrayList<RegistratedClient> clients = new ArrayList<>();
        Timer timer = new Timer();
        OrderStatusChecker checker = new OrderStatusChecker(orders);
        timer.schedule(checker, 0, 60 * 1000); // Проверка каждые 60 секунд
        // Ввод нескольких ресторанов
        System.out.println("Введите информацию о ресторанах:");
        do {
            Restaurant restaurant = Restaurant.createRestaurant();
            restaurants.put(restaurant.getName(), restaurant);
            while (true) {
                try {
                    result = 3;
                    while(result != 0 && result !=1){
                        System.out.println("Если хотите продолжить вводить рестораны нажмите 1, если закончить 0");
                        result = input.nextInt();
                        input.nextLine();
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Ошибка: введено некорректное значение. Пожалуйста, введите целое число.");
                    input.nextLine(); // очистка буфера ввода
                }
            }
        }while(result != 0);
        // Регистрация клиента и создание заказа

        Client client = Client.createClient();
        while(true) {
            try {
                do{
                    System.out.println("Если хотите зарегистрировать клиента в базу нажмите 1,если закончить 0");
                    result = input.nextInt();
                    input.nextLine();
                }while(result != 0 && result != 1);
                if(result == 1){
                    RegistratedClient registratedClient = RegistratedClient.createClient(client);
                    clients.add(registratedClient);
                    registratedClient.setOrderCount(registratedClient.getOrderCount()+1);
                    registratedClient.changeLevel();
                    System.out.println("Тепереь у вас есть скидка в: "+ registratedClient.getDiscount()*100
                            + "% с каждым заказом она будет увеличиваться!");
                    Order order = Order.createOrder(restaurants,registratedClient);

                    int discount = order.makeDiscount(registratedClient.getDiscount());
                    orders.add(order);
                    System.out.println("Заказ создан:");
                    System.out.println(order);
                    System.out.println("Скидка составила " + discount);

                }else{
                    Order order = Order.createOrder(restaurants,client);
                    orders.add(order);
                    System.out.println("Заказ создан:");
                    System.out.println(order);
                }

                break;
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введено некорректное значение. Пожалуйста, введите целое число.");
                input.nextLine(); // очистка буфера ввода
            }
        }

        System.out.println("Если хотите продолжить введите что угодно");
        input.nextLine(); // очистка буфера ввода

        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1. Ввести новый ресторан и его меню");
            System.out.println("2. Ввести клиента и создать заказ");
            System.out.println("3. Посмотреть статус заказа по ID");
            System.out.println("4. Посмотреть список заказов");
            System.out.println("0. Завершить программу");
            Order order;
            while (true) {
                try {
                    do{
                        result = input.nextInt();
                        input.nextLine();
                        if(result <0 || result > 4){
                            System.out.println("Выберите действие из списка");
                        }
                    }while(result < 0 || result > 4);
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Ошибка: введено некорректное значение. Пожалуйста, введите целое число.");
                    input.nextLine(); // очистка буфера ввода
                }
            }
            switch (result) {
                case 1:
                    Restaurant restaurant = Restaurant.createRestaurant();
                    restaurants.put(restaurant.getName(), restaurant);
                    break;
                case 2:

                    do{
                        System.out.println("Введите 1 если вы зарегистрированны и" +
                                " хотите войти или 0 если хотите продолжить как новый клиент");
                        result = input.nextInt();
                        input.nextLine();
                    }while(result!=0 && result!=1);
                    //с регистрацией
                    if(result==1){
                        int missmatch = 0;
                        System.out.println("Введите идентификатор клиента:");
                        int id = input.nextInt();
                        input.nextLine();
                        boolean wrong = true;
                        boolean correct = false;
                        for (RegistratedClient registratedClient : clients ) {
                            if(registratedClient.getId() == id){
                                //если id совпал
                                do{
                                    System.out.println("Введите пароль:");
                                    String password = input.nextLine();
                                    correct = registratedClient.checkPassword(password);
                                    if(correct){
                                        System.out.println("Вы успешно вошли в аккаунт!");
                                        order = Order.createOrder(restaurants,registratedClient);
                                        registratedClient.setOrderCount(registratedClient.getOrderCount()+1);
                                        registratedClient.changeLevel();

                                        int discount = order.makeDiscount(registratedClient.getDiscount());
                                        orders.add(order);
                                        System.out.println("Заказ создан:");
                                        System.out.println(order);
                                        System.out.println("Скидка составила " + discount);
                                    }else{
                                        System.out.println("Неверный пароль, попробуйте еще раз");
                                        missmatch++;
                                        if(missmatch == 3){
                                            System.out.println("Вы превысили максимальное количество попыток");
                                        }else{
                                            System.out.println("Количество оставшихся попыток: "+ (3-missmatch));
                                        }
                                    }
                                }while(!correct && missmatch < 3 );
                                wrong = false;
                                break;
                            }
                        }
                        //если ни один идентификатор не совпал
                        if(wrong){
                            System.out.println("Клиент с таким ID не найден");
                        }
                        //без регистрации
                    }else{
                        client = Client.createClient();
                        while(true) {
                            try {
                                do{
                                    System.out.println("Если хотите зарегистрировать клиента в базу нажмите 1,если закончить 0");
                                    result = input.nextInt();
                                    input.nextLine();
                                }while(result != 0 && result != 1);
                                if(result == 1){
                                    RegistratedClient registratedClient = RegistratedClient.createClient(client);
                                    clients.add(registratedClient);
                                    System.out.println("Тепереь у вас есть скидка в: "+ registratedClient.getDiscount()*100
                                            + "% с каждым заказом она будет увеличиваться!");
                                    order = Order.createOrder(restaurants,registratedClient);
                                    registratedClient.setOrderCount(registratedClient.getOrderCount()+1);
                                    registratedClient.changeLevel();
                                    int discount = order.makeDiscount(registratedClient.getDiscount());
                                    orders.add(order);
                                    System.out.println("Заказ создан:");
                                    System.out.println(order);
                                    System.out.println("Скидка составила " + discount);
                                }else{
                                    order = Order.createOrder(restaurants,client);
                                    orders.add(order);
                                    System.out.println("Заказ создан:");
                                    System.out.println(order);
                                }
                                break;
                            } catch (InputMismatchException e) {
                                System.out.println("Ошибка: введено некорректное значение. Пожалуйста, введите целое число.");
                                input.nextLine(); // очистка буфера ввода
                            }
                        }
                    }
                    break;
                case 3:
                    // Посмотреть статус заказа по ID
                    while(true) {
                        try{
                            System.out.println("Введите ID заказа:");
                            int id = input.nextInt();
                            input.nextLine();
                            boolean wrong = true;
                            for (Order order1 : orders) {
                                if (order1.getId() == id) {
                                    wrong = false;
                                    switch(order1.getStatus()){
                                        case 0:
                                            System.out.println("Заказ принят");
                                            break;
                                        case 1:
                                            System.out.println("Заказ готовится");
                                            break;
                                        case 2:
                                            System.out.println("Заказ готов");
                                            break;
                                        case 3:
                                            System.out.println("Заказ в пути");
                                            break;
                                        default:
                                    }
                                    break;
                                }
                            }
                            if(wrong){
                                System.out.println("Заказ с таким ID не найден");
                            }
                            break;
                        }catch (InputMismatchException e){
                            System.out.println("Ошибка: введено некорректное значение. Пожалуйста, введите целое число.");
                            input.nextLine(); // очистка буфера ввода
                        }
                    }
                    break;
                case 4:

                    // Посмотреть список заказов с сортировкой
                    while (true) {
                        try {
                            System.out.println("Выберите способ сортировки:");
                            System.out.println("1. По ID заказа");
                            System.out.println("2. По статусу готовности");
                            System.out.println("3. По стоимости");


                            int sortChoice = input.nextInt();
                            input.nextLine();

                            switch (sortChoice) {
                                case 1:
                                    Collections.sort(orders);
                                    break;
                                case 2:
                                    orders.sort( new OrderStatusComparator());
                                    break;
                                case 3:
                                    orders.sort( new OrderCostComparator());
                                    break;
                                default:
                                    System.out.println("Неверный выбор. Пожалуйста, выберите способ сортировки из списка.");
                                    continue;
                            }
                            System.out.println("Список заказов:");
                            for (Order order1 : orders) {
                                System.out.println(order1);
                            }
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Ошибка: введено некорректное значение. Пожалуйста, введите целое число.");
                            input.nextLine(); // очистка буфера ввода
                        }
                    }
                    break;
                case 0:
                    System.out.println("Завершение программы.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, выберите действие из списка.");
            }
        }
    }
}
class Client{
    private String name;
    private String phoneNumber;
    private String adress;
    public Client(String name,String phoneNumber,String adress){

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.adress = adress;
    }
    public Client(Client client){
        this.name = client.name;
        this.phoneNumber = client.phoneNumber;
        this.adress = client.adress;
    }
    public static Client createClient() {
        Scanner input = new Scanner(System.in);
        String[] info = getInfo();
        return new Client(info[0],info[1],info[2]);
    }
    public Client(){
        name = "Underfind";
        phoneNumber = "Underfind";
        adress = "Underfind";

    }
    protected static String[] getInfo(){
        Scanner input = new Scanner(System.in);

        System.out.println("Введите информацию о клиенте:");
        System.out.println("Введите имя клиента:");
        String clientName = input.nextLine();
        System.out.println("Введите номер телефона клиента:");
        String clientPhoneNumber = input.nextLine();
        System.out.println("Введите адрес клиента:");
        String clientAddress = input.nextLine();
        return new String[]{clientName,clientPhoneNumber,clientAddress};

    }
    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAdress() {
        return adress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

}
abstract class Identifiable{
    public abstract int getId();

}
class Dish{
    private String name;
    private int price;
    public Dish(String name,int price){
        this.name = name;
        this.price = price;
    }
    public static Dish createDish() {
        Scanner input = new Scanner(System.in);

        System.out.println("Введите название блюда:");
        String dishName = input.nextLine();
        int dishPrice;
        while (true) {
            try {
                System.out.println("Введите цену блюда:");
                dishPrice= input.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введено некорректное значение. Пожалуйста, введите целое число.");
                input.nextLine(); // очистка буфера ввода
            }
        }

        return new Dish(dishName, dishPrice);
    }
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name + ": " + price;
    }
}
class Restaurant implements Cloneable{
    private String name;
    private String adress;
    private ArrayList<Dish> menu;
    public Restaurant(String name, String adress) {
        this.name = name;
        this.adress = adress;
        menu = new ArrayList<>();

    }
    public Restaurant(Restaurant restaurant){
        this.name = restaurant.name;
        this.adress = restaurant.adress;
        menu = new ArrayList<>(restaurant.getMenu());
    }
    public static Restaurant createRestaurant() {
        Scanner input = new Scanner(System.in);
        int result = 3;
        System.out.println("Введите название ресторана:");
        String restaurantName = input.nextLine();
        System.out.println("Введите адрес ресторана:");
        String restaurantAddress = input.nextLine();
        Restaurant restaurant = new Restaurant(restaurantName, restaurantAddress);

        // Ввод меню для ресторана
        System.out.println("Введите блюда в меню ресторана " + restaurant.getName() + ": ");
        do {
            Dish dish = Dish.createDish();
            restaurant.addDish(dish);
            System.out.println("Блюдо " + dish.getName() + " добавлено в ресторан: " + restaurant.getName());
            while (true) {
                try {
                    result =3;
                    while(result !=0 && result !=1){
                        System.out.println("Если хотите продолжить вводить меню нажмите 1, если закончить 0");
                        result = input.nextInt();
                        input.nextLine();
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Ошибка: введено некорректное значение. Пожалуйста, введите целое число.");
                    input.nextLine(); // очистка буфера ввода
                }
            }

        } while (result != 0);

        return restaurant;
    }
    //реализация поверхносного копирования
    public Restaurant clone() throws CloneNotSupportedException{
        Restaurant clone = (Restaurant)super.clone();
        clone.menu = this.menu;
        clone.name = this.name;
        clone.adress = this.adress;
        return clone;
    }
    public ArrayList<Dish> getMenu() {
        return menu;
    }
    public void addDish(Dish d){
        menu.add(d);
    }
    public Restaurant(){
        name = "undefind";
        adress = "undefind";
        menu = new ArrayList<>();
    }
    public String getName() {
        return name;
    }

    public String getAdress() {
        return adress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
class Basket implements Cloneable{
    private Integer totalCoast;
    private ArrayList<Dish> dishes;
    public Basket(int totalCoast,ArrayList<Dish> dishes){
        this.dishes = new ArrayList<>(dishes);
        this.totalCoast = totalCoast;
    }
    public Basket(Basket basket){
        this.dishes = new ArrayList<>(basket.dishes);
        this.totalCoast = basket.totalCoast;
    }
    public Basket(){
        totalCoast = 0;
        dishes = new ArrayList<>();
    }
    public Integer getTotalCoast() {
        return totalCoast;
    }
    //реализация реализация глубокого копирования копирования
    public Basket clone() throws CloneNotSupportedException{
        Basket clone = (Basket)super.clone();
        clone.dishes = new ArrayList<>(this.dishes);
        clone.totalCoast = this.totalCoast;
        return clone;
    }
    public ArrayList<Dish> getDishes() {
        return dishes;
    }
    public void setTotalCoast(int totalCoast) {
        this.totalCoast = totalCoast;
    }
    public void addDish(Dish d){
        dishes.add(d);
        totalCoast += d.getPrice();
    }
    public Dish getDish(int index){
        return dishes.get(index);
    }
    public void setDishes(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }
    public int makeDiscount(double discount){
        if(discount <0 || discount>1){
            System.out.println("Ошибка: невозможная скидка ");
            return 0;
        }else{
            int result;
            result = (int) (totalCoast * discount);
            totalCoast = (int) (totalCoast * (1 - discount));
            return result;
        }
    }
    @Override
    public String toString() {
        return "Корзина:" +
                "\nитоговая цена: " + totalCoast +
                "\nсписок блюд: " + dishes ;
    }
}
class Order extends Identifiable implements Comparable<Order>{
    private static int count = 0;
    private final int id;
    private Client client;
    private Basket basket;
    private Restaurant restaurant;
    private int status;
    public Order(Client client,Basket basket,Restaurant restaurant){
        this.id = count;
        count++;
        this.client = new Client(client);
        this.basket = new Basket(basket);
        this.restaurant = new Restaurant(restaurant);
        status = 0;
    }
    public Order(){
        id = count;
        count++;
        client = new Client();
        basket = new Basket();
        restaurant = new Restaurant();
        status = 0;
    }
    public int makeDiscount(double discount){

        return basket.makeDiscount(discount);
    }
    public static Order createOrder(HashMap<String,Restaurant> restaurants,Client client) {
        Scanner input = new Scanner(System.in);

        System.out.println("Выберите ресторан для заказа:");
        for (Map.Entry<String, Restaurant> entry : restaurants.entrySet()) {
            System.out.println(entry.getKey());
        }
        boolean wrong = true;
        String selectedRestaurant = input.nextLine();
        Restaurant chosenRestaurant = null;
        while(wrong) {
            for (Map.Entry<String, Restaurant> entry : restaurants.entrySet()) {
                if (entry.getKey().equals(selectedRestaurant)) {
                    chosenRestaurant = restaurants.get(selectedRestaurant);
                    wrong = false;
                    break;
                }
            }
            if(wrong){
                System.out.println("Выбранный ресторан не найден. Пожалуйста, выберите ресторан из списка.");
                selectedRestaurant = input.nextLine();
            }
        }


        Basket basket = new Basket();
        System.out.println("Выберите блюда для добавления в корзину:");
        for (Dish dish : chosenRestaurant.getMenu()) {
            System.out.println(dish.getName() + " - " + dish.getPrice());
        }
        System.out.println("Введите название блюда для добавления в корзину (или 'конец' для завершения):");
        String dishChoice = input.nextLine();
        boolean isCorrect = false;
        while (!dishChoice.equals("конец")) {
            for (Dish dish : chosenRestaurant.getMenu()) {
                if (dish.getName().equals(dishChoice)) {
                    basket.addDish(dish);
                    System.out.println("Блюдо \"" + dish.getName() + "\" добавлено в заказ");
                    isCorrect = true;
                }
            }
            if(!isCorrect){
                System.out.println("Таких блюд нет в меню");
            }else{
                isCorrect = false;
            }


            dishChoice = input.nextLine();
        }

        return new Order(client, basket, chosenRestaurant);
    }
    public static int getCount() {
        return count;
    }
    public int compareTo(Order o) {
        return id - o.id;
    }
    public int getId() {
        return id;
    }
    public Client getClient() {
        return client;
    }
    public Basket getBasket() {
        return basket;
    }
    public String toString() {
        return "Заказ \n" +
                "ID заказа: " + getId() +
                "\nКлиент: " + getClient().getName() +
                "\nРесторан: " + getRestaurant().getName() +
                "\n" + getBasket().toString();
    }
    public Restaurant getRestaurant() {
        return restaurant;
    }
    public int getStatus() {
        return status;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public void setBasket(Basket basket) {
        this.basket = basket;
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
class OrderStatusComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return o1.getStatus() - o2.getStatus();
    }
}
class OrderCostComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return o1.getBasket().getTotalCoast() - o2.getBasket().getTotalCoast();
    }
}
class OrderStatusChecker extends TimerTask {
    private ArrayList<Order> orders;
    public OrderStatusChecker(ArrayList<Order> orders) {
        super();
        this.orders = orders;
    }
    @Override
    public void run() {
        Iterator<Order> iterator = orders.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            order.setStatus(order.getStatus()+1);
            if (order.getStatus() == 4) {
                iterator.remove();
            }
        }
    }
}
class RegistratedClient extends Client{
    private final int id;
    private static int count = 0;
    private String password;
    private int level;
    private int orderCount;
    public RegistratedClient(Client client, String password){
        super(client.getName(), client.getPhoneNumber(),client.getAdress());
        this.id = count;
        count++;
        makePassword();
        level = 0;
        orderCount = 0;
        System.out.println("ID клиента: "+id);
    }
    public RegistratedClient(String name, String phoneNumber, String adress,String password){
        super(name, phoneNumber, adress);
        this.id = count;
        count++;
        level = 0;
        orderCount = 0;
        this.password = password;
    }
    public void setOrderCount(int orderCount){
        this.orderCount = orderCount;
    }
    public int getOrderCount(){
        return orderCount;
    }
    public static RegistratedClient createClient(){
        String[] info = getInfo();
        String password = makePassword();
        return new RegistratedClient(info[0],info[1],info[2],password);
    }
    public static RegistratedClient createClient(Client client){
        String password = makePassword();
        return new RegistratedClient(client.getName(), client.getPhoneNumber(), client.getAdress(),password);
    }
    public void changeLevel(){
        level = (int)Math.sqrt(orderCount);
    }
    public double getDiscount(){
        return Math.min(level*0.1+0.01,0.5+0.01);
    }
    public void soutLevelDiscount(){
        System.out.println("Скидка клиента с ID: "+id+ "равна " + getDiscount()*100+ "%");
    }
    private static String makePassword(){
            boolean eq = false;
            Scanner input = new Scanner(System.in);
            String password1 = "";
            String password2;
            while(!eq){
                System.out.println("Введите пароль:");
                password1 = input.nextLine();
                System.out.println("Повторите пароль:");
                password2 = input.nextLine();
                eq = password1.equals(password2);
                if(eq){
                    System.out.println("Пароль для клиента установлен");

                }
                else{
                    System.out.println("Пароли не совпадают, повторите процедуру");
                }
            }
            return password1;

    }
    public void setNewPassword(String oldPassword){
        if(oldPassword.equals(password)){
            boolean eq = false;
            Scanner input = new Scanner(System.in);
            String password1;
            String password2;
            while(!eq) {
                System.out.println("Введите новый пароль:");
                password1 = input.nextLine();
                System.out.println("Повторите новый пароль:");
                password2 = input.nextLine();
                eq = password1.equals(password2);
                if (eq) {
                    System.out.println("Пароль для клиента с ID: " + id + "установлен");
                    password = password1;
                } else {
                    System.out.println("Пароли не совпадают, повторите процедуру");
                }
            }
        }else{
            System.out.println("Неверный пароль");
        }

    }
    public int getId() {
        return id;
    }
    public static int getCount() {
        return count;
    }
    public int getLevel() {
        return level;
    }
    public boolean checkPassword(String password){
        return password.equals(this.password);
    }
}