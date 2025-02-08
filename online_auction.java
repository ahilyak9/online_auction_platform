package dsp;


import java.util.Scanner;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

// Binary Search Tree Node and Tree classes
class BSTNode {
    Product product;
    BSTNode left, right;

    public BSTNode(Product product) {
        this.product = product;
        this.left = this.right = null;
    }
}

class BinarySearchTree {
    private BSTNode root;

    public void insert(Product product) {
        root = insertRec(root, product);
    }

    private BSTNode insertRec(BSTNode root, Product product) {
        if (root == null) {
            root = new BSTNode(product);
            return root;
        }
        if (product.getName().compareTo(root.product.getName()) < 0) {
            root.left = insertRec(root.left, product);
        } else if (product.getName().compareTo(root.product.getName()) > 0) {
            root.right = insertRec(root.right, product);
        }
        return root;
    }

    public Product search(String productName) {
        return searchRec(root, productName);
    }

    private Product searchRec(BSTNode root, String productName) {
        if (root == null || root.product.getName().equals(productName)) {
            return root != null ? root.product : null;
        }
        if (productName.compareTo(root.product.getName()) < 0) {
            return searchRec(root.left, productName);
        }
        return searchRec(root.right, productName);
    }

    public void inorderTraversal() {
        inorderRec(root);
    }

    private void inorderRec(BSTNode root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.println("Product: " + root.product.getName() + ", Status: " + root.product.getStatus());
            inorderRec(root.right);
        }
    }
}

// Product Class
class Product {
    private String id;
    private String name;
    private String category;
    private String status;
    private double minBid;
    private double highestBid;
    private Buyer highestBidder;
    private Thread biddingThread;
    private Thread trackingThread;

    public Product(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.status = "Not Started";
        this.highestBid = 0.0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }

    public double getMinBid() {
        return minBid;
    }

    public double getHighestBid() {
        return highestBid;
    }

    public Buyer getHighestBidder() {
        return highestBidder;
    }

    public void setMinBid(double minBid) {
        this.minBid = minBid;
    }

    public void placeBid(double bid, Buyer buyer) {
        if (bid > highestBid && bid >= minBid) {
            highestBid = bid;
            highestBidder = buyer;
            System.out.println("New highest bid: $" + highestBid);
        } else {
            System.out.println("Bid must be higher than the current highest bid and minimum bid.");
        }
    }

    public void startBidding() {
        if (biddingThread == null) {
            biddingThread = new Thread(() -> {
                System.out.println("Bidding started for " + name);
                try {
                    Thread.sleep(30000); // Bidding period of 30 seconds
                } catch (InterruptedException e) {
                    System.out.println("Bidding interrupted.");
                }
                System.out.println("Bidding ended for " + name + ". Final highest bid: $" + highestBid);
                status = "Bidding Closed";
            });
            status = "Bidding in Progress";
            biddingThread.start();
        } else {
            System.out.println("Bidding is already in progress for this product.");
        }
    }

    public void trackPackage() {
        if (trackingThread == null) {
            trackingThread = new Thread(() -> {
                String[] stages = {"Ready to Deliver", "Out for Delivery", "Delivered"};
                for (String stage : stages) {
                    try {
                        Thread.sleep(10000); // Update every 10 seconds
                    } catch (InterruptedException e) {
                        System.out.println("Tracking interrupted.");
                    }
                    status = stage;
                    System.out.println("Tracking " + name + ": " + stage);
                }
            });
            trackingThread.start();
        } else {
            System.out.println("Tracking already in progress for this product.");
        }
    }

    public void updateStatus(String newStatus) {
        status = newStatus;
    }
}

// Admin, Seller, and Buyer classes
class Admin {
    private String username = "admin";
    private String password = "admin123";

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void setMinBid(Product product, double minBid) {
        product.setMinBid(minBid);
        System.out.println("Minimum bid set to $" + minBid + " for " + product.getName());
    }

    public void trackPackage(Product product) {
        product.trackPackage();
    }

    public void approveProduct(Product product) {
        product.updateStatus("Approved");
        System.out.println("Product " + product.getName() + " approved.");
    }

    public void viewProducts(BinarySearchTree bst) {
        System.out.println("Admin Viewing All Products:");
        bst.inorderTraversal();
    }
}

class Seller {
    private List<Product> productHistory = new ArrayList<>();

    public boolean login(String username, String password) {
        System.out.println("Seller logged in with username: " + username);
        return true;
    }

    // Adding a product to the seller's product list
    public Product addProduct(String name) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Product ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Product Category: ");
        String category = scanner.nextLine();

        Product newProduct = new Product(id, name, category);
        productHistory.add(newProduct);
        System.out.println("Product " + name + " added to your listing.");
        return newProduct;
    }

    // Viewing the product history for the seller
    public void viewProductHistory() {
        System.out.println("Product History:");
        for (Product product : productHistory) {
            System.out.println("ID: " + product.getId() + ", Name: " + product.getName() + ", Category: " + product.getCategory() + ", Status: " + product.getStatus());
        }
    }

    // Sorting products by their category and displaying them
    public void sortProductsByCategory() {
        productHistory.sort(Comparator.comparing(Product::getCategory));
        System.out.println("Products sorted by category:");
        viewProductHistory();
    }
}

class Buyer {
    private String username;
    private String password;

    public Buyer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void viewProduct(Product product) {
        System.out.println("Viewing Product: " + product.getName() + ", Status: " + product.getStatus() +
                ", Min Bid: $" + product.getMinBid() + ", Highest Bid: $" + product.getHighestBid());
    }

    public void placeBid(Product product) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your bid amount: ");
        double bid = scanner.nextDouble();
        product.placeBid(bid, this);
        System.out.println("do anyone wnat to place next bid yes(1)/no(0)");
        int chh = scanner.nextInt();
        if(chh==0){
             try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("bid interrupted");
            }
            System.out.println("THIS item is sold to "+  username +" \nPROCEED WITH PAYMENT!!!");
        }
    } 
    public void makePayment(Product product) {
        if (this == product.getHighestBidder()) {
            System.out.println("Processing payment of $" + product.getHighestBid());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Processing payment was interrupted.");
            }
            System.out.println("Payment successful for $" + product.getHighestBid());
        } else {
            System.out.println("Only the highest bidder can make payment.");
        }
    }

    public void trackPackage(Product product) {
        product.trackPackage();
    }
}


class Graph {
    private HashMap<Product, List<Product>> adjList = new HashMap<>();

    public void addEdge(Product from, Product to) {
        adjList.putIfAbsent(from, new ArrayList<>());
        adjList.putIfAbsent(to, new ArrayList<>());
        adjList.get(from).add(to);
        System.out.println("Edge added from " + from.getName() + " to " + to.getName());
    }

    public void displayGraph() {
        System.out.println("Product Dependency Graph:");
        for (Product product : adjList.keySet()) {
            System.out.print(product.getName() + " -> ");
            for (Product connectedProduct : adjList.get(product)) {
                System.out.print(connectedProduct.getName() + " ");
            }
            System.out.println();
        }
    }
}

class Main {
    private static LinkedList<Product> products = new LinkedList<>();
    private static BinarySearchTree bst = new BinarySearchTree();
    private static Admin admin = new Admin();
    private static Seller seller = new Seller();
    private static HashMap<String, Buyer> buyers = new HashMap<>();
    private static Graph productGraph = new Graph(); // Initialize the Graph

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Admin Login");
            System.out.println("2. Seller Login");
            System.out.println("3. Buyer Login for Bidding");
            System.out.println("4. Add Product Dependency (Graph Edge)");
            System.out.println("5. Display Product Graph");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Admin username: ");
                    String adminUsername = scanner.nextLine();
                    System.out.print("Enter Admin password: ");
                    String adminPassword = scanner.nextLine();
                    if (admin.login(adminUsername, adminPassword)) {
                        System.out.println("Admin logged in.");
                        adminMenu(scanner);
                    } else {
                        System.out.println("Invalid Admin credentials.");
                    }
                    break;

                case 2:
                    System.out.print("Enter Seller username: ");
                    String sellerUsername = scanner.nextLine();
                    System.out.print("Enter Seller password: ");
                    String sellerPassword = scanner.nextLine();
                    if (seller.login(sellerUsername, sellerPassword)) {
                        System.out.print("Enter Product Name to add: ");
                        String productName = scanner.nextLine();
                        Product newProduct = seller.addProduct(productName);
                        products.add(newProduct);
                        bst.insert(newProduct);
                    }
                    break;

                case 3:
                    System.out.print("Enter Buyer username: ");
                    String buyerUsername = scanner.nextLine();
                    System.out.print("Enter Buyer password: ");
                    String buyerPassword = scanner.nextLine();

                    Buyer currentBuyer = buyers.get(buyerUsername);
                    if (currentBuyer == null) {
                        currentBuyer = new Buyer(buyerUsername, buyerPassword);
                        buyers.put(buyerUsername, currentBuyer);
                        System.out.println("New Buyer account created.");
                    }

                    // Displaying Products for Bidding
                    System.out.print("Choose Product Name to Bid on: ");
                    String bidProductName = scanner.nextLine();
                    Product bidProduct = bst.search(bidProductName);
                    if (bidProduct != null) {
                        currentBuyer.viewProduct(bidProduct);
                        System.out.println("1. Place Bid\n2. Make Payment\n3. Track Package\nChoose an option:");
                        int buyerChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (buyerChoice) {
                            case 1:
                                // Call the placeBid method (this is where the 3-second window is managed)
                                currentBuyer.placeBid(bidProduct);
                                break;
                            case 2:
                                currentBuyer.makePayment(bidProduct);
                                break;
                            case 3:
                                currentBuyer.trackPackage(bidProduct);
                                break;
                            default:
                                System.out.println("Invalid option.");
                        }
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter source product name for dependency: ");
                    String sourceName = scanner.nextLine();
                    Product sourceProduct = bst.search(sourceName);

                    System.out.print("Enter destination product name for dependency: ");
                    String destName = scanner.nextLine();
                    Product destProduct = bst.search(destName);

                    if (sourceProduct != null && destProduct != null) {
                        productGraph.addEdge(sourceProduct, destProduct);
                    } else {
                        System.out.println("One or both products not found.");
                    }
                    break;

                case 5:
                    productGraph.displayGraph();
                    break;

                case 6:
                    System.out.println("Exiting the application.");
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void adminMenu(Scanner scanner) {
        boolean adminExit = false;
        while (!adminExit) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Set Minimum Bid for Product");
            System.out.println("2. Track Product Package");
            System.out.println("3. Approve Product for Bidding");
            System.out.println("4. View All Products");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int adminChoice = scanner.nextInt();
            scanner.nextLine();

            switch (adminChoice) {
                case 1:
                    System.out.print("Enter Product Name: ");
                    String productName = scanner.nextLine();
                    Product product = bst.search(productName);
                    if (product != null) {
                        System.out.print("Enter Minimum Bid Amount: ");
                        double minBid = scanner.nextDouble();
                        admin.setMinBid(product, minBid);
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;

                case 2:
                    System.out.print("Enter Product Name: ");
                    String trackProductName = scanner.nextLine();
                    Product trackProduct = bst.search(trackProductName);
                    if (trackProduct != null) {
                        admin.trackPackage(trackProduct);
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter Product Name: ");
                    String approveProductName = scanner.nextLine();
                    Product approveProduct = bst.search(approveProductName);
                    if (approveProduct != null) {
                        admin.approveProduct(approveProduct);
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;

                case 4:
                    admin.viewProducts(bst);
                    break;

                case 5:
                    System.out.println("Admin logged out.");
                    adminExit = true;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}