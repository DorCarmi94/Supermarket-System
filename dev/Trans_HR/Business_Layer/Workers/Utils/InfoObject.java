package Trans_HR.Business_Layer.Workers.Utils;

public class InfoObject {
    private String message;
    private boolean isSucceeded;

    public InfoObject(String message, boolean isSucceeded){
        this.message = message;
        this.isSucceeded = isSucceeded;
    }

    public void setIsSucceeded(boolean condition){
        this.isSucceeded = condition;
    }

    public boolean isSucceeded() {
        return isSucceeded;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
