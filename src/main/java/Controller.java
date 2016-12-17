import Model.State;
import Model.StateBuilder;

/**
 * Created by n_buga on 17.12.16.
 */
public class Controller {
    public static void main(String[] args) {
        State curState = new StateBuilder().build();
        for (int i = 0; i < 10; i++) {
            curState.doTurn();
            curState.print();
        }
    }

/*    public static State doTurn(State curState) {
        curState.doTurn();
    } */
}
