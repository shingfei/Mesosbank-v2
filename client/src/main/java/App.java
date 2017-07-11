import javax.swing.*;

/**
 * Created by SF on 16-6-2017.
 */
public class App {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {

            public void run()
            {
                new ReadCard(8025);
            }
        });
    }
}
