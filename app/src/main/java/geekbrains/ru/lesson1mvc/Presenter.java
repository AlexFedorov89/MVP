package geekbrains.ru.lesson1mvc;

public class Presenter {
    Model model = new Model();
    private MainView view;

    public Presenter(MainView view) {
        this.view = view;
    }

    private void updateNewElementValue(int id) {
        int newValue = model.getElementValueAtIndex(id) + 1;
        model.setElementValueAtIndex(id, newValue);
        view.setButtonText(id, newValue);
    }

    public void onClick(int id) {
        updateNewElementValue(id);
    }
}
