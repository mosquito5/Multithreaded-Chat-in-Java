import javafx.scene.control.TextField;

public class LimitedTextField extends TextField {

    private final int maxLenght;

    public LimitedTextField(int maxLenght) {
        this.maxLenght = maxLenght;
    }

    @Override
    public void replaceText(int start, int end, String text) {
        super.replaceText(start, end, text);
        verify();

    }

    @Override
    public void replaceSelection(String text) {
        super.replaceSelection(text);
        verify();
    }

    private void verify() {
        if(getText().length() > maxLenght) {
            setText((getText().substring(0, maxLenght)));
        }
    }
}