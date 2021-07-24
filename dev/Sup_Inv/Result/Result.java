package Sup_Inv.Result;

public class Result<T> {
    private ResultTags myTag;
    private T value;
    private String my_message;

    private Result(ResultTags tag,String message)
    {
        myTag=tag;
        this.my_message=message;
    }

    private Result(ResultTags tag,String message, T value)
    {
        this(tag, message);
        this.value = value;
    }

    public static <T> Result<T> makeOk(String message, T value)
    {
        return new Result<T>(ResultTags.OK,message, value);
    }

    public static <T> Result<T> makeFailure(String message)
    {
        return new Result<T>(ResultTags.Failure,message);
    }

    public boolean isOk() {
        return myTag==ResultTags.OK;
    }

    public boolean isFailure() {
        return myTag==ResultTags.Failure;
    }

    public String getMessage() {
        return my_message;
    }

    public T getValue(){
        return value;
    }
}

enum ResultTags
{
    OK,
    Failure
}