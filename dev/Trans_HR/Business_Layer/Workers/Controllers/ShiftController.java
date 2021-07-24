package Trans_HR.Business_Layer.Workers.Controllers;


import Trans_HR.Business_Layer.Service;
import Trans_HR.Business_Layer.Transportations.Utils.Buisness_Exception;
import Trans_HR.Business_Layer.Workers.Modules.Shift;
import Trans_HR.Business_Layer.Workers.Modules.Worker.Worker;
import Trans_HR.Business_Layer.Workers.Utils.InfoObject;
import Trans_HR.Business_Layer.Workers.Utils.enums;
import Trans_HR.Data_Layer.Mapper;
import Trans_HR.Interface_Layer.Workers.SystemInterfaceWorkers;
import Trans_HR.Presentation_Layer.Workers.HR;

import java.text.SimpleDateFormat;
import java.util.*;


public class ShiftController {

    private int snFactory;
    private int currentStoreSN;

    public ShiftController() {
        this.snFactory = 0;
        this.currentStoreSN = -1;
    }

    public InfoObject printAllShits() {
        InfoObject infoObject = new InfoObject("", true);
        if (Service.getInstance().getShiftHistory(getCurrentStoreSN()).isEmpty()) {
            infoObject.setIsSucceeded(false);
            infoObject.setMessage("There are no shifts to display");
            return infoObject;
        }
        System.out.println("Select shift by SN");
        List<Shift> shiftToDisplay = new LinkedList<>(Service.getInstance().getShiftHistory(getCurrentStoreSN()).values());
        Collections.sort(shiftToDisplay);
        for (Shift shift : shiftToDisplay) {
            SimpleDateFormat day = new SimpleDateFormat("dd-MM-yyyy");
            String date = day.format(shift.getDate());
            System.out.println(shift.getShiftSn() + ". Date: " + date + " Type: " + shift.getShiftType());
        }
        return infoObject;
    }

    private int getSnFactory() {
        return ++this.snFactory;
    }

    public void getShifts(){
        Mapper.getInstance().getAllShifts(this.currentStoreSN);
    }

    public void getShifts(int storeSn){
        Mapper.getInstance().getAllShifts(storeSn);
    }

    public void resetSnFactory(){
        this.snFactory = 0;
    }

    public InfoObject validateNewShiftDate(Date date, enums shiftType) {
        InfoObject infoObject = new InfoObject("", true);
        for (Shift shifty : Service.getInstance().getShiftHistory(getCurrentStoreSN()).values()) {
            if ((shifty.getDate().compareTo(date) == 0) && shifty.getShiftType().equals(shiftType)) {
                infoObject.setIsSucceeded(false);
                infoObject.setMessage("There is already a shift on this date");
                return infoObject;
            }
        }
        Date toDay = new Date();
        if (date.compareTo(toDay) < 0) {
            infoObject.setIsSucceeded(false);
            infoObject.setMessage("Date already passed");
            return infoObject;
        }
        return infoObject;
    }

    public InfoObject createShift(String shiftType, int managerSn, String listOfWorkersSn, String dateToParse) {
        InfoObject infoObject = new InfoObject("", true);
        enums sType;
        try {
            sType = enums.valueOf(shiftType);
        } catch (Exception e) {
            infoObject.setIsSucceeded(false);
            infoObject.setMessage("Invalid shift type");
            return infoObject;
        }

        Date date = parseDate(dateToParse);
        if (date == null) {
            infoObject.setIsSucceeded(false);
            infoObject.setMessage("Invalid date format");
            return infoObject;
        }
        infoObject = validateNewShiftDate(date, sType);
        if (!infoObject.isSucceeded()) {
            return infoObject;
        }

        if (!(Service.getInstance().getWorkerList(getCurrentStoreSN()).containsKey(managerSn))) {
            infoObject.setIsSucceeded(false);
            infoObject.setMessage("Invalid manager SN");
            return infoObject;
        }
        if (!(Service.getInstance().getWorkerList(getCurrentStoreSN()).get(managerSn).getWorkerJobTitle().toUpperCase().equals("MANAGER"))) {
            infoObject.setIsSucceeded(false);
            infoObject.setMessage("Invalid manager SN");
            return infoObject;
        }
        Worker manager = Service.getInstance().getWorkerList(getCurrentStoreSN()).get(managerSn);
        String[] workersSn;
        List<Worker> workersListOfCurrentShift = new LinkedList<>();
     //hadar   List<Integer> workersDB =new LinkedList<>();
        infoObject = validateManagerConstrains(manager, sType, date);
        if (!infoObject.isSucceeded()) {
            return infoObject;
        }
        workersListOfCurrentShift.add(manager);
        if (!listOfWorkersSn.toUpperCase().equals("NONE")) {
            try {
                workersSn = listOfWorkersSn.split(",");
            } catch (Exception e) {
                workersSn = null;
            }
            if (workersSn == null) {
                infoObject.setIsSucceeded(false);
                infoObject.setMessage("Invalid workers SN format input");
                return infoObject;
            }
            if (!workersSn[0].equals("")) {
                // validate workers SN
                for (String workerSn : workersSn) {
                    if (!(Service.getInstance().getWorkerList(getCurrentStoreSN()).containsKey(Integer.parseInt(workerSn)))) {
                        infoObject.setIsSucceeded(false);
                        infoObject.setMessage("There is no worker with serial number " + Integer.parseInt(workerSn));
                        return infoObject;
                    }
                }

                // validate workers starting date and shift date
                for (String workerSn : workersSn) {
                    Date workersDate = Service.getInstance().getWorkerList(getCurrentStoreSN()).get(Integer.parseInt(workerSn)).getWorkerStartingDate();
                    if (date.compareTo(workersDate) < 0) {
                        infoObject.setIsSucceeded(false);
                        infoObject.setMessage("This worker : " + Service.getInstance().getWorkerList(getCurrentStoreSN()).get(Integer.parseInt(workerSn)).getWorkerName() + " Start working only from " +
                                Service.getInstance().getWorkerList(getCurrentStoreSN()).get(Integer.parseInt(workerSn)).getWorkerStartingDate());
                        return infoObject;
                    }
                }

                // validate workers constrains  with shift date
                for (String workerSn : workersSn) {
                    Worker workerToAdd = Service.getInstance().getWorkerList(getCurrentStoreSN()).get(Integer.parseInt(workerSn));
                    if (!(workerToAdd.available(date, sType))) {
                        infoObject.setIsSucceeded(false);
                        infoObject.setMessage(workerToAdd.getWorkerName() + " Can't work on this shift because of his constrains");
                        return infoObject;
                    }
                    if (workersListOfCurrentShift.contains(workerToAdd)) {
                        infoObject.setIsSucceeded(false);
                        infoObject.setMessage(workerToAdd.getWorkerName() + " Already in this shift. Cant add the same worker twice");
                        return infoObject;
                    }
                    //workersDB.add(workerToAdd.getWorkerSn());
                    workersListOfCurrentShift.add(workerToAdd);
                }
            }
        } else {
            workersListOfCurrentShift = new LinkedList<>();
        }
        Shift shiftToAdd = new Shift(date, sType, manager, workersListOfCurrentShift, getSnFactory(),getCurrentStoreSN());

        int type = shiftTypeToInteger(shiftType);

        Mapper.getInstance().insertShift(date,type,managerSn,shiftToAdd.getShiftSn(),getCurrentStoreSN());
        for(Worker x : workersListOfCurrentShift){
            Mapper.getInstance().insert_Shift_Workers(x.getWorkerSn(),shiftToAdd.getShiftSn());
        }

        Service.getInstance().getShiftHistory().put(shiftToAdd.getShiftSn(), shiftToAdd);
        infoObject.setMessage("Shift created successfully");
        return infoObject;
    }

    private Integer shiftTypeToInteger(String shiftType) {
        Integer DBshiftType=0;
        switch (shiftType) {
            case "MORNING":
                DBshiftType = 1;
                break;

            case "NIGHT":
                DBshiftType = 2;
                break;
        }
        return DBshiftType;
    }

    public InfoObject printShift(int shiftIndex) {
        InfoObject infoObject = new InfoObject("", true);
        if (!(Service.getInstance().getShiftHistory(getCurrentStoreSN()).containsKey(shiftIndex))) {
            infoObject.setMessage("There is no shift with this SN");
            infoObject.setIsSucceeded(false);
            return infoObject;
        }
        Shift shift = Service.getInstance().getShiftHistory(getCurrentStoreSN()).get(shiftIndex);
        shift.printShift();
        if (shift.getShiftWorkers().size() == 1) {
            System.out.println("\n" + "Workers: No workers for this shift");
        } else {
            System.out.println("\n" + "Workers: ");
            for (Worker worker : shift.getShiftWorkers()) {
                if ((shift.getDate().compareTo(new Date()) < 0) | Service.getInstance().getWorkerList(getCurrentStoreSN()).containsValue(worker)) {
                    if (!worker.getWorkerJobTitle().toUpperCase().equals("MANAGER")) {
                        worker.printWorker();
                    }
                }
            }
        }
        System.out.println("\n");
        return infoObject;
    }

    private Date parseDate(String _date) {
        Date date;
        try {
            date = new SimpleDateFormat("d-MM-yyyy").parse(_date);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    private InfoObject validateManagerConstrains(Worker manager, enums shiftType, Date shiftDate) {
        InfoObject infoObject = new InfoObject("", true);
        Date workersDate = manager.getWorkerStartingDate();
        if (shiftDate.compareTo(workersDate) < 0) {
            infoObject.setIsSucceeded(false);
            infoObject.setMessage("This worker : " + manager.getWorkerName() + " Start working only from " +
                    manager.getWorkerStartingDate());
            return infoObject;
        }

        if (!(manager.available(shiftDate, shiftType))) {
            infoObject.setIsSucceeded(false);
            infoObject.setMessage(manager.getWorkerName() + " Can't work on this shift because of his constrains");
            return infoObject;
        }

        return infoObject;
    }

    public InfoObject removeLaterShiftForFiredManagerByManagerSn(int workerSn) {
        InfoObject infoObject = new InfoObject("", true);

        Date today = new Date();
        List<Shift> listOfShiftsRemoved = new LinkedList<>();
        for (Shift shift : Service.getInstance().getShiftHistory(getCurrentStoreSN()).values()) {
            Date shiftDate = shift.getDate();
            // shift date occurs after today
            if (shiftDate.compareTo(today) > 0) {
                if (shift.getManager().getWorkerSn() == workerSn) {
                    listOfShiftsRemoved.add(shift);
                }
            }
        }
        for (Shift shiftToRemove : listOfShiftsRemoved) {
            Service.getInstance().getShiftHistory().remove(shiftToRemove.getShiftSn());
        }
        System.out.println("This shifts has been removed : \n");
        for (Shift deletedShift : listOfShiftsRemoved) {
            deletedShift.printShift();
            System.out.println();
        }

        return infoObject;
    }

    public int getCurrentStoreSN() {
        return currentStoreSN;
    }

    public void setCurrentStoreSN(int currentStoreSN) {
        this.currentStoreSN = currentStoreSN;
    }

    public boolean isStorekeeperAvailable(Date date,String shiftType){
        enums selectedDay;
        enums sType;
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
        try {
            selectedDay = enums.valueOf(dayOfWeek);
        }
        catch (Exception e){
            System.out.println("No such day");
            return false;
        }
        try{
            sType = enums.valueOf(shiftType);
        }
        catch (Exception e){
            System.out.println("No such shift type");
            return false;
        }

        for(Shift shift : Service.getInstance().getShiftHistory(getCurrentStoreSN()).values()){
            if(shift.getDate().compareTo(date) == 0 && shift.getShiftType().compareTo(sType) == 0 ){
                for(Worker worker : shift.getShiftWorkers()){
                    if(worker.getWorkerJobTitle().toLowerCase().equals("storekeeper")){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isDriverAvailable(Date date,String shiftType){
        enums selectedDay;
        enums sType;
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
        try {
            selectedDay = enums.valueOf(dayOfWeek);
        }
        catch (Exception e){
            System.out.println("No such day");
            return false;
        }
        try{
            sType = enums.valueOf(shiftType);
        }
        catch (Exception e){
            System.out.println("No such shift type");
            return false;
        }

        for(Shift shift : Service.getInstance().getShiftHistory(getCurrentStoreSN()).values()){
            if(shift.getDate().compareTo(date) == 0 && shift.getShiftType().compareTo(sType) == 0 ){
                for(Worker worker : shift.getShiftWorkers()){
                    if(worker.getWorkerJobTitle().toLowerCase().equals("driver")){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void getAllSN() {

        int Sn = Mapper.getInstance().getShiftSn();
        this.snFactory = ++Sn;
    }

    public boolean isThereShiftWithThisDate(Date date, String shiftType){
        enums selectedDay;
        enums sType;
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
        try {
            selectedDay = enums.valueOf(dayOfWeek);
        }
        catch (Exception e){
            System.out.println("No such day");
            return false;
        }
        try{
            sType = enums.valueOf(shiftType);
        }
        catch (Exception e){
            System.out.println("No such shift type");
            return false;
        }

        for(Shift shift : Service.getInstance().getShiftHistory(getCurrentStoreSN()).values()){
            if(shift.getDate().compareTo(date) == 0 && shift.getShiftType().compareTo(sType) == 0 ){
                return true;
            }
        }
        return false;
    }

    public boolean isThereShiftWithStoreKeeperAndDriver(Date date, String shiftType){
        enums selectedDay;
        boolean driver =false;
        boolean storekeeper =false;
        enums sType;
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
        try {
            selectedDay = enums.valueOf(dayOfWeek);
        }
        catch (Exception e){
            System.out.println("No such day");
            return false;
        }
        try{
            sType = enums.valueOf(shiftType);
        }
        catch (Exception e){
            System.out.println("No such shift type");
            return false;
        }

        for(Shift shift : Service.getInstance().getShiftHistory(getCurrentStoreSN()).values()){
            if(shift.getDate().compareTo(date) == 0 && shift.getShiftType().compareTo(sType) == 0 ){
                for(Worker worker : shift.getShiftWorkers()){
                    if(worker.getWorkerJobTitle().toLowerCase().equals("driver")){
                        driver = true;
                    }
                    if(worker.getWorkerJobTitle().toLowerCase().equals("storekeeper")){
                        storekeeper = true;
                    }
                }
            }
        }
        return driver && storekeeper;
    }

    public Shift getShift(Date date,String shiftType){
        Shift shiftToReturn = null;
        enums selectedDay;
        boolean driver =false;
        boolean storekeeper =false;
        enums sType;
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
        try {
            selectedDay = enums.valueOf(dayOfWeek);
        }
        catch (Exception e){
            System.out.println("No such day");
            return null;
        }
        try{
            sType = enums.valueOf(shiftType);
        }
        catch (Exception e){
            System.out.println("No such shift type");
            return null;
        }

        for(Shift shift : Service.getInstance().getShiftHistory(getCurrentStoreSN()).values()){
            if(shift.getDate().compareTo(date) == 0 && shift.getShiftType().compareTo(sType) == 0 ){
                return shift;
            }
        }
        return  shiftToReturn;
    }


    public boolean isThereIsStoreKeeper(Date date, String shiftType){
        enums selectedDay;
        boolean driver =false;
        boolean storekeeper =false;
        enums sType;
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
        try {
            selectedDay = enums.valueOf(dayOfWeek);
        }
        catch (Exception e){
            System.out.println("No such day");
            return false;
        }
        try{
            sType = enums.valueOf(shiftType);
        }
        catch (Exception e){
            System.out.println("No such shift type");
            return false;
        }

        for(Shift shift : Service.getInstance().getShiftHistory(getCurrentStoreSN()).values()){
            if(shift.getDate().compareTo(date) == 0 && shift.getShiftType().compareTo(sType) == 0 ){
                for(Worker worker : shift.getShiftWorkers()){
                    if(worker.getWorkerJobTitle().toLowerCase().equals("storekeeper")){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isThereIsDriver(Date date, String shiftType){
        enums selectedDay;
        boolean driver =false;
        boolean storekeeper =false;
        enums sType;
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
        try {
            selectedDay = enums.valueOf(dayOfWeek);
        }
        catch (Exception e){
            System.out.println("No such day");
            return false;
        }
        try{
            sType = enums.valueOf(shiftType);
        }
        catch (Exception e){
            System.out.println("No such shift type");
            return false;
        }

        for(Shift shift : Service.getInstance().getShiftHistory(getCurrentStoreSN()).values()){
            if(shift.getDate().compareTo(date) == 0 && shift.getShiftType().compareTo(sType) == 0 ){
                for(Worker worker : shift.getShiftWorkers()){
                    if(worker.getWorkerJobTitle().toLowerCase().equals("driver")){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // for rubin ---------------------------------------------------------------------------------------------------------------------------------------
    // for rubin ---------------------------------------------------------------------------------------------------------------------------------------
    // for rubin ---------------------------------------------------------|------------------------------------------------------------------------------
    // for rubin ---------------------------------------------------------|------------------------------------------------------------------------------
    // for rubin ---------------------------------------------------------|------------------------------------------------------------------------------
    // for rubin ---------------------------------------------------------|------------------------------------------------------------------------------
    // for rubin ---------------------------------------------------------|------------------------------------------------------------------------------
    // for rubin --------------------------------------------------------\ /-----------------------------------------------------------------------------


    public void checkShiftWithDriverAndStoreKeeper(String date, String shiftType) {

        System.out.println(date);
        System.out.println(shiftType);

        enums selectedDay;
        enums sType;
        Date _date = parseDate(date);
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(_date);

        try {
            selectedDay = enums.valueOf(dayOfWeek.toUpperCase());
        } catch (Exception e){
            System.out.println("No such day");
            return;
        }
        try{
            sType = enums.valueOf(shiftType.toUpperCase());
        } catch (Exception e){
            System.out.println("No such shift type");
            return;
        }


        if(isThereShiftWithThisDate(_date,shiftType)){
            // there is a shift with this date
            // need just to add to the workers of this shift
            if(!(isThereShiftWithStoreKeeperAndDriver(_date,shiftType))){
                if(isThereIsStoreKeeper(_date,shiftType)){
                    if(isDriverAvailable(_date,shiftType)){
                        System.out.println("Choose driver SN from the list to add to the shift");
                        InfoObject infoObject = Service.getInstance().getWorkerController().printAllWorkersByJobAvailableInThisDate(date,shiftType,"driver");
                        if(!infoObject.isSucceeded()){
                            System.out.println(infoObject.getMessage());
                        } else {
                            Scanner sc = new Scanner(System.in);
                            while (!sc.hasNextInt()) {
                                System.out.println("Invalid input, please try again");
                                sc.next();
                            }
                            int selectedOption = sc.nextInt();
                            Worker workerToAdd= null;
                            for(Worker workers :  Service.getInstance().getWorkerList(getCurrentStoreSN()).values()){
                                if(workers.getWorkerSn() == selectedOption){
                                    workerToAdd = workers;
                                }
                            }
                            Shift shiftToEdit = getShift(_date,shiftType);
                            shiftToEdit.getShiftWorkers().add(workerToAdd);
                            Mapper.getInstance().insert_Shift_Workers(workerToAdd.getWorkerSn(),shiftToEdit.getShiftSn());
                            /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                            /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                            /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                            /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        }
                    } else {
                        System.out.println("There are no available driver that can work for this shift.");
                    }
                } else {
                     if(isThereIsDriver(_date,shiftType)){
                         System.out.println("Choose storekeeper SN from the list to add to the shift");
                         InfoObject infoObject = Service.getInstance().getWorkerController().printAllWorkersByJobAvailableInThisDate(date,shiftType,"storekeeper");
                         if(!infoObject.isSucceeded()){
                             System.out.println(infoObject.getMessage());
                         } else {
                             Scanner sc = new Scanner(System.in);
                             while (!sc.hasNextInt()) {
                                 System.out.println("Invalid input, please try again");
                                 sc.next();
                             }
                             int selectedOption = sc.nextInt();
                             Worker workerToAdd= null;
                             for(Worker workers :  Service.getInstance().getWorkerList(getCurrentStoreSN()).values()){
                                 if(workers.getWorkerSn() == selectedOption){
                                     workerToAdd = workers;
                                 }
                             }
                             Shift shiftToEdit = getShift(_date,shiftType);
                             shiftToEdit.getShiftWorkers().add(workerToAdd);
                             Mapper.getInstance().insert_Shift_Workers(workerToAdd.getWorkerSn(),shiftToEdit.getShiftSn());
                             /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                             /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                             /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                             /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                         }
                     } else {
                         System.out.println("Choose driver SN from the list to add to the shift");
                         InfoObject infoObject = Service.getInstance().getWorkerController().printAllWorkersByJobAvailableInThisDate(date,shiftType,"driver");
                         if(!infoObject.isSucceeded()){
                             System.out.println(infoObject.getMessage());
                         } else {
                             Scanner sc = new Scanner(System.in);
                             while (!sc.hasNextInt()) {
                                 System.out.println("Invalid input, please try again");
                                 sc.next();
                             }
                             int selectedOption = sc.nextInt();
                             Worker workerToAdd= null;
                             for(Worker workers :  Service.getInstance().getWorkerList(getCurrentStoreSN()).values()){
                                 if(workers.getWorkerSn() == selectedOption){
                                     workerToAdd = workers;
                                 }
                             }
                             Shift shiftToEdit = getShift(_date,shiftType);
                             shiftToEdit.getShiftWorkers().add(workerToAdd);
                             Mapper.getInstance().insert_Shift_Workers(workerToAdd.getWorkerSn(),shiftToEdit.getShiftSn());
                             /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                             /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                             /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                             /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                             /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                         }

                         System.out.println("Choose storekeeper SN from the list to add to the shift");
                         infoObject = Service.getInstance().getWorkerController().printAllWorkersByJobAvailableInThisDate(date,shiftType,"storekeeper");
                         if(!infoObject.isSucceeded()){
                             System.out.println(infoObject.getMessage());
                         } else {
                             Scanner sc = new Scanner(System.in);
                             while (!sc.hasNextInt()) {
                                 System.out.println("Invalid input, please try again");
                                 sc.next();
                             }
                             int selectedOption = sc.nextInt();
                             Worker workerToAdd= null;
                             for(Worker workers :  Service.getInstance().getWorkerList(getCurrentStoreSN()).values()){
                                 if(workers.getWorkerSn() == selectedOption){
                                     workerToAdd = workers;
                                 }
                             }
                             Shift shiftToEdit = getShift(_date,shiftType);
                             shiftToEdit.getShiftWorkers().add(workerToAdd);
                             Mapper.getInstance().insert_Shift_Workers(workerToAdd.getWorkerSn(),shiftToEdit.getShiftSn());
                             /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                             /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                             /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                             /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                             /// add to DATABASE !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                         }
                     }
                }
            }
        } else {
            System.out.println("There is not shift on this date");
            System.out.println("Would you like to move to HR position and create a new shift? (choose 1 / 2)");
            System.out.println("1. Yes");
            System.out.println("2. No");

            Scanner sc = new Scanner(System.in);
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input, please try again");
                sc.next();
            }
            int selectedOption = sc.nextInt();

            if(selectedOption == 1){
                try {
                    HR.addShift(sc);
                } catch (Buisness_Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void printAllShiftsForThisStore() {
            Mapper.getInstance().getAllShifts(getCurrentStoreSN());
            for(Shift shiftToPrint : Service.getInstance().getShiftHistory(getCurrentStoreSN()).values()){
                shiftToPrint.printShift();
            }
    }
}

