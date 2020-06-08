import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class SplitPanel extends JSplitPane {
    private Board board;

    Timer sliderTimer;
    TimerTask menuTask;
    int divider;

    public SplitPanel(Board board, SlideMenu menu) {
        super(JSplitPane.HORIZONTAL_SPLIT,
                true,
                board,
                menu);
        this.board = board;
        initSplitPane();
    }

    private void initSplitPane() {
        initSliderTimer();
        setPreferredSize(new Dimension(board.getBoardWidth(), board.getBoardHeight()));
        setOneTouchExpandable(false);
        setDividerLocation(board.getBoardWidth());
        setDividerSize(0);
    }

    public TimerTask createTask(String name) throws IllegalArgumentException {
        TimerTask timerTask;
        divider = this.getDividerLocation();

        if (name.equals("show")) {
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    divider -= 8;
                    setDividerLocation(divider);
                    if (divider <= (int) (board.getBoardWidth() * 0.75)) {
                        menuTask.cancel();
                    }
                }
            };
        } else if (name.equals("hide")) {
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    divider += 8;
                    setDividerLocation(divider);
                    if (divider >= board.getBoardWidth()) {
                        menuTask.cancel();
                    }
                }
            };
        } else {
            throw new IllegalArgumentException();
        }

        return timerTask;
    }

    public void hideMenu() {
        board.togglePause();
        menuTask.cancel();
        menuTask = createTask("hide");
        sliderTimer.scheduleAtFixedRate(menuTask, 0, 5);
    }

    public void showMenu() {
        board.togglePause();
        menuTask.cancel();
        menuTask = createTask("show");
        sliderTimer.scheduleAtFixedRate(menuTask, 0, 5);
    }

    private void initSliderTimer() {
        sliderTimer = new Timer("SilderTimer");
        menuTask = createTask("show");
    }
}
