package Trans_HR.Presentation_Layer.Workers;

import Presentation.CurrStore;
import Trans_HR.Business_Layer.Service;
import Trans_HR.Business_Layer.Transportations.Utils.Buisness_Exception;
import Trans_HR.Business_Layer.Workers.Utils.InfoObject;
import Trans_HR.Data_Layer.Mapper;
import Trans_HR.Interface_Layer.Workers.SystemInterfaceWorkers;

import java.util.Scanner;

import static Presentation.WelcomeMenu.chooseJob;

public class HR {

    public static void Menu() throws Buisness_Exception {
        //System.out.println("Welcome! Enter you choice");
        Scanner sc = new Scanner(System.in);
        try {
            systemStart(sc);
        }
        catch (Exception e){

        }
    }

    public static void run() throws Buisness_Exception {

        System.out.println("1. Run tests");
        System.out.println("2. Start system");
        System.out.println("Enter 0 to go back");
        Scanner sc = new Scanner(System.in);
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        int selectedOption = sc.nextInt();
        if (selectedOption == 1) {
            runTest();
            System.out.println();
        }
        if (selectedOption == 2) {
            start(sc);
        }
        if (selectedOption == 0) {
            throw new Buisness_Exception("Going back");
        }
        if (selectedOption < 0 || selectedOption > 2) {
            System.out.println("Invalid input, please try again");
            run();
        }
        run();

    }

    public static void runTest() throws Buisness_Exception {
//        System.out.println("Starting Tests\n");
//        final LauncherDiscoveryRequest request =
//                LauncherDiscoveryRequestBuilder.request()
//                        .selectors(selectClass(projectTests.class))
//                        .build();
//
//        final Launcher launcher = LauncherFactory.create();
//        final SummaryGeneratingListener listener = new SummaryGeneratingListener();
//
//        launcher.registerTestExecutionListeners(listener);
//        launcher.execute(request);
//
//        final TestExecutionSummary summary = listener.getSummary();
//
//        final long testsFoundCount = summary.getTestsFoundCount();
//
//        System.out.println("");
//
//
//        final long succeededTests = summary.getTestsSucceededCount();
//        System.out.println("Total tests Succeeded  " + succeededTests + "/" + testsFoundCount);
//
//        final long testsSkippedCount = summary.getTestsSkippedCount();
//        System.out.println("tests Skipped Count  " + testsSkippedCount + "/" + testsFoundCount);
//
//        final long testsFailed = summary.getTestsFailedCount();
//        System.out.println("tests Failed Count  " + testsFailed + "/" + testsFoundCount);
//
//        final long testAborted = summary.getTestsAbortedCount();
//        System.out.println("tests Aborted Count  " + testAborted + "/" + testsFoundCount);
//        System.out.println();

    }

    public static void start(Scanner sc)  throws Buisness_Exception{
        try {
            systemStart(sc);
        }catch (Exception e){

        }
    }

    private static void systemStart(Scanner sc) throws Buisness_Exception{
        //new new new system -- delete everything and mapper.init.
        //old system new iteration -- no special action, just takes data from db when it needs to.
        //new system with data -- new new new system and insert some data.

        System.out.println("Welcome to SuperLee - HR");
        //System.out.println("1. Start system");
        //System.out.println("1. Start new system");
        //System.out.println("2. Start new system with data");
        //System.out.println("3. Resume last session ");
        System.out.println("Enter 0 to go back");
        getAllSN();
        chooseStore(sc);
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        int selectedOption = sc.nextInt();
//        if (selectedOption == 1) {
//            getAllSN();
//            chooseStore(sc);
//        }
//        if (selectedOption == 0) {
//            throw new Buisness_Exception("Going back");
//        }
//        if (selectedOption == 1) { ///new system
//            cleanSuperLeeDB();
//            try {
//                SystemInterfaceWorkers.getInstance().initConstants();
//            }
//            catch (Exception e){
//
//            }
//            chooseStore(sc);
//        }
//        if (selectedOption == 2) { ///new system with data
//            cleanSuperLeeDB();
//            try {
//                SystemInterfaceWorkers.getInstance().initConstants();
//            }
//            catch (Exception e){
//            }
//            initSuperLeeWithWorkers();
//            chooseStore(sc);
//        }
//        if (selectedOption == 3) {
//            getAllSN();
//            chooseStore(sc);
//        }
        if (selectedOption < 0) {
            System.out.println("Invalid input, please try again");
            systemStart(sc);
        }
        chooseStore(sc);

    }

    public static void getAllSN() {
        SystemInterfaceWorkers.getInstance().getAllSN();
    }

    private static void cleanSuperLeeDB() {
        SystemInterfaceWorkers.getInstance().clearDB();
    }

    public static void chooseStore(Scanner sc) throws Buisness_Exception{
        try {
            Mapper.getInstance().init();
        }
        catch (Exception e){

        }
        System.out.println("1. Choose store");
        System.out.println("2. Add store");
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        SystemInterfaceWorkers.getInstance().getStores();
        int userChoose = sc.nextInt();
        if (userChoose == 1) {
            if (!(SystemInterfaceWorkers.getInstance().printAllStores())) {
                // Empty
                chooseStore(sc);
            } else {
                // Store.count > 0
                while (!sc.hasNextInt()) {
                    System.out.println("Invalid input, please try again");
                    sc.next();
                }
                userChoose = sc.nextInt();
                CurrStore.getInstance().setStore_id(userChoose);
                if (!(SystemInterfaceWorkers.getInstance().setCurrentStore(userChoose))) {
                    chooseStore(sc);
                } else{
                    //workingLoop(sc);

                }
            }
        } else if (userChoose == 2) {

            Scanner scan = new Scanner(System.in);
            System.out.println("Please choose the name of the store"); //chose area
            String name = scan.nextLine();
            System.out.println("Please choose city of the store");
            String city = scan.nextLine();
            System.out.println("Please choose street of the store");
            String street = scan.nextLine();
            System.out.println("Please choose street's number of the store");
            String number = scan.nextLine();
            System.out.println("Please enter name of contact for the store");
            String name_of_contact = scan.nextLine();
            System.out.println("Please enter contact's person phone");
            String phone = scan.nextLine();
            System.out.println("Please choose store's area (A/B/C/D)");
            String area = scan.nextLine();
            boolean result = false;
            result = SystemInterfaceWorkers.getInstance().addNewStore(name, city, street, number, name_of_contact, phone, area);
            if (result) {
                System.out.println("The store was added successfully\n");
                chooseStore(sc);
            } else {
                System.out.println("Input error\n");
                chooseStore(sc);
            }
        } else if(userChoose == 0){
            throw new Buisness_Exception("Going back");
        } else {
            System.out.println("Invalid input, please try again");
            chooseStore(sc);
        }
    }

    public static void workingLoop(Scanner sc) throws Buisness_Exception {
        printMenu();
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        int userChoose = sc.nextInt();
        while (true) {
            switch (userChoose) {
                case 1: // add shift
                    addShift(sc);
                    break;
                case 2: // Display shift
                    displayShifts(sc);
                    break;
                case 3: // add worker
                    addWorker(sc);
                    break;
                case 4: // Display Worker
                    displayWorkers(sc);
                    break;
                case 0: // quit
                    chooseJob(sc);
                default:
                    System.out.println("Invalid input, please try again");
            }
            printMenu();
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input, please try again");
                sc.next();
            }
            userChoose = sc.nextInt();
        }
    }

    private static void printMenu() throws Buisness_Exception{
        System.out.println("Enter your next choice:");
        System.out.println("1. Add shift");
        System.out.println("2. Display shifts");
        System.out.println("3. Add worker");
        System.out.println("4. Display workers");
        System.out.println("Enter 0 to go back");
    }

    public static void addShift(Scanner sc) throws Buisness_Exception {
        SystemInterfaceWorkers.getInstance().getShifts();
        System.out.println("Please select a date - dd-mm-yyyy");
        while (!sc.hasNext()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        String date = sc.next();
        System.out.println("Please select shift type Morning or Night");
        while (!sc.hasNext()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        String shiftType = sc.next().toUpperCase(); // { Morning , Night }
        System.out.println("Please select manager SN for the shift");
        checkResponse(SystemInterfaceWorkers.getInstance().printAllManagersAvailableInDates(date, shiftType), sc);
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        int selectedManagerSn = sc.nextInt();
        System.out.println("Please select workers SN's for the shift");
        InfoObject infoObject = SystemInterfaceWorkers.getInstance().printAllWorkersAvailableInDates(date, shiftType);
        if (infoObject.getMessage().equals("There are no available workers")) {
            System.out.println(infoObject.getMessage());
            checkResponse(SystemInterfaceWorkers.getInstance().createShift(shiftType, selectedManagerSn, "", date), sc);
        } else {
            String chosenWorkersSn = sc.nextLine();
            chosenWorkersSn = sc.nextLine();
            chosenWorkersSn = chosenWorkersSn.replaceAll(" ", "");
            checkResponse(SystemInterfaceWorkers.getInstance().createShift(shiftType, selectedManagerSn, chosenWorkersSn, date), sc);
        }
    }

    public static void displayShifts(Scanner sc) throws Buisness_Exception {
        SystemInterfaceWorkers.getInstance().getShifts();
        checkResponse(SystemInterfaceWorkers.getInstance().printAllShifts(), sc);
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        int shiftSn = sc.nextInt();
        if (shiftSn == 0) {
            workingLoop(sc);
        }
        checkResponse(SystemInterfaceWorkers.getInstance().printShift(shiftSn), sc);
    }

    public static void addWorker(Scanner sc) throws Buisness_Exception {
        SystemInterfaceWorkers.getInstance().getWorkers();
        System.out.println("Enter worker Id:");
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        int workerId = sc.nextInt();
        System.out.println("Enter worker name:");
        String workerName = sc.nextLine();
        workerName = sc.nextLine();
        System.out.println("Enter phone:");
        while (!sc.hasNext()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        String workerPhoneNumber = sc.nextLine();
        System.out.println("Enter bank account:");
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        int workerBankAccount = sc.nextInt();
        System.out.println("Enter salary:");
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        int workerSalary = sc.nextInt();
        System.out.println("Enter starting date - dd-mm-yyyy");
        while (!sc.hasNext()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        String dateOfStart = sc.next();
        System.out.println("Enter job title:");
        while (!sc.hasNext()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        String workerJobTitle = sc.next();
        String licenses ="";
        if (workerJobTitle.toUpperCase().equals("DRIVER")) {
            System.out.println("Enter driver's license (C/C1)");
            while (!sc.hasNext()) {
                System.out.println("Invalid input, please try again");
                sc.next();
            }
            licenses = sc.next();
        }
        System.out.println("Enter constrains: - Day-ShiftType , Day-ShiftType, etc...");
        while (!sc.hasNext()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        String constrains = sc.nextLine();
        constrains = sc.nextLine();
        constrains = constrains.replaceAll(" ", "");
        if (workerJobTitle.toUpperCase().equals("DRIVER")) {
            checkResponse(SystemInterfaceWorkers.getInstance().addDriver(workerId, workerName, workerPhoneNumber, workerBankAccount, workerSalary, dateOfStart, workerJobTitle, constrains, licenses), sc);
        } else {
            checkResponse(SystemInterfaceWorkers.getInstance().addWorker(workerId, workerName, workerPhoneNumber, workerBankAccount, workerSalary, dateOfStart, workerJobTitle, constrains), sc);
        }
    }

    public static void displayWorkers(Scanner sc) throws Buisness_Exception {
        SystemInterfaceWorkers.getInstance().getWorkers();
        checkResponse(SystemInterfaceWorkers.getInstance().printAllWorkers(), sc);
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        int workerSn = sc.nextInt();
        if (workerSn == 0) {
            workingLoop(sc);
        }
        checkResponse(SystemInterfaceWorkers.getInstance().printWorkerBySn(workerSn), sc);
        System.out.println("Choose action: ");
        System.out.println("1. Edit worker constrains");
        System.out.println("2. Edit worker salary");
        if(SystemInterfaceWorkers.getInstance().isDriver(workerSn)){
            System.out.println("3. Add driver license");
        }
        //System.out.println("3. Fire worker");
        System.out.println("Enter 0 to go back to main menu");
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        int userChooseForWorker = sc.nextInt();

        switch (userChooseForWorker) {
            case 1: // Edit constrains
                EditWorkerConstrains(sc, workerSn);
                break;
            case 2: // Edit salary
                EditWorkerSalary(sc, workerSn);
                break;
            case 3:
                if(SystemInterfaceWorkers.getInstance().isDriver(workerSn)){
                    AddDriverLicense(sc,workerSn);
                }else{
                    System.out.println("Invalid input, going back to display workers");
                    displayWorkers(sc);
                }
         /*   case 3: // Fire worker
                checkResponse(SystemInterfaceWorkers.getInstance().removeWorkerBySn(workerSn), sc);
                checkResponse(SystemInterfaceWorkers.getInstance().removeLaterShiftForFiredManagerByManagerSn(workerSn), sc);
                break;*/
            case 0: // quit
                workingLoop(sc);
                break;
            default:
                System.out.println("Invalid input, going back to display workers");
                displayWorkers(sc);
        }
    }

    private static void AddDriverLicense(Scanner sc, int workerSn) throws Buisness_Exception {
        System.out.println("Enter new License");
        String License = sc.nextLine();
        License = sc.nextLine();
        checkResponse(SystemInterfaceWorkers.getInstance().addNewLicense(workerSn,License),sc);
    }

    private static void EditWorkerSalary(Scanner sc, int workerSn) throws Buisness_Exception {
        System.out.println("Enter new salary");
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        int newSalary = sc.nextInt();
        checkResponse(SystemInterfaceWorkers.getInstance().setNewSalaryBySn(workerSn, newSalary), sc);
    }

    private static void EditWorkerConstrains(Scanner sc, int workerSn) throws Buisness_Exception {
        checkResponse(SystemInterfaceWorkers.getInstance().resetWorkerConstrainsBySn(workerSn), sc);
        System.out.println("Enter constrains: - Day-ShiftType , Day-ShiftType, etc...");
        String constrains = sc.nextLine();
        constrains = sc.nextLine();
        constrains = constrains.replaceAll("\\s+", "");
        checkResponse(SystemInterfaceWorkers.getInstance().editWorkerConstrainsBySn(workerSn, constrains), sc);
        checkResponse(SystemInterfaceWorkers.getInstance().printWorkerConstrainsBySn(workerSn), sc);
    }

    public static void initSuperLeeWithWorkers() throws Buisness_Exception {

        Service.getInstance().site_controller.addsite("store","Nesspresso","Holon","Krauze","12","Hadar","04","A");
        Service.getInstance().site_controller.addsite("store","Nesspresso_2","Holon","Krauze","12","Hadar","04","A");

        Service.getInstance().getWorkerController().setCurrentStoreSN(1);
        Service.getInstance().getShiftController().setCurrentStoreSN(1);
        Service.getInstance().getWorkerController().addWorker(100, "Andrey Palman", "100", 123, 100, "15-04-2020", "Cashier", "");
        Service.getInstance().getWorkerController().addWorker(101, "Hadar Kor", "101", 124, 2500, "15-04-2020", "Manager", "");
        Service.getInstance().getWorkerController().addWorker(102, "Tomer Hacham", "102", 125, 10000, "15-04-2020", "Storekeeper", "");
        Service.getInstance().getWorkerController().addWorker(103, "Amit Rubin", "102", 125, 10000, "15-04-2020", "Storekeeper", "");
        Service.getInstance().getWorkerController().addWorker(104, "Reut Levy", "102", 125, 10000, "15-04-2020", "Storekeeper", "");
        Service.getInstance().getWorkerController().addWorker(105, "Hadas Zaira", "102", 125, 10000, "15-04-2020", "Storekeeper", "");
        Service.getInstance().getWorkerController().addWorker(106, "Roi Benhus", "102", 125, 10000, "15-04-2020", "Storekeeper", "");
        Service.getInstance().getShiftController().createShift("MORNING", 2, "1,3", "19-12-2020");
        Service.getInstance().getShiftController().createShift("NIGHT", 2, "1,3,4", "19-12-2020");
        Service.getInstance().getShiftController().createShift("MORNING", 2, "1,3,5", "20-12-2020");
        Service.getInstance().getShiftController().createShift("NIGHT", 2, "1,3,6,7", "20-12-2020");
        Service.getInstance().getShiftController().createShift("MORNING", 2, "1,3,4,5,6,7", "21-12-2020");
        Service.getInstance().getShiftController().createShift("NIGHT", 2, "1,3,5", "21-12-2020");
        Service.getInstance().getShiftController().createShift("MORNING", 2, "1,6,7", "22-12-2020");
        Service.getInstance().getShiftController().createShift("NIGHT", 2, "1,4,5", "22-12-2020");


        Service.getInstance().getWorkerController().setCurrentStoreSN(2);
        Service.getInstance().getShiftController().setCurrentStoreSN(2);
        Service.getInstance().getWorkerController().addWorker(100, "Andrey Palman", "100", 123, 100, "15-04-2020", "Cashier", "");
        Service.getInstance().getWorkerController().addWorker(101, "Hadar Kor", "101", 124, 2500, "15-04-2020", "Manager", "");
        Service.getInstance().getWorkerController().addWorker(102, "Tomer Hacham", "102", 125, 10000, "15-04-2020", "Storekeeper", "");
        Service.getInstance().getShiftController().createShift("MORNING", 9, "8,10", "19-12-2020");
        Service.getInstance().getShiftController().createShift("NIGHT", 9, "10", "19-12-2020");
        Service.getInstance().getShiftController().createShift("MORNING", 9, "8", "20-12-2020");

    }

    private static void checkResponse(InfoObject infoObject, Scanner sc)  throws Buisness_Exception{
        if (!infoObject.isSucceeded()) {
            System.out.println(infoObject.getMessage());
            workingLoop(sc);
        } else {
            if (!infoObject.getMessage().equals("")) {
                System.out.println(infoObject.getMessage());
            }
        }
    }
}
