package cnlpexercises;

public class TodoException extends RuntimeException {

    @Override
    public void printStackTrace() {
        System.err.println("Your code isn't finished at:");
        super.printStackTrace();
    }

    public static void todo() {
        throw new TodoException();
    }
}
