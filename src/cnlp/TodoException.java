package cnlp;

public class TodoException extends Exception {

    @Override
    public void printStackTrace() {
        System.err.println("Your code isn't finished at:");
        super.printStackTrace();
    }
}
