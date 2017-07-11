
/**
 * Created by shing on 28-3-2017.
 */
        import gnu.io.*;
        import nl.hro.rick.mesosbank.api.*;
        import org.glassfish.jersey.jackson.JacksonFeature;

        import javax.swing.*;
        import java.awt.*;
        import java.awt.event.*;
        import javax.ws.rs.client.Client;
        import javax.ws.rs.client.ClientBuilder;
        import javax.ws.rs.client.Entity;
        import javax.ws.rs.client.WebTarget;
        import javax.ws.rs.core.MediaType;
        import java.util.Timer;
        import java.util.TimerTask;
        import java.io.*;
        import java.util.Enumeration;
        import java.util.concurrent.TimeUnit;

public class ReadCard implements SerialPortEventListener {
    private CardLayout cardTest = new CardLayout();
    int biljet10 = 10;
    int biljet20 = 10;
    int biljet50 = 10;
    int hoeveelheid10;
    int hoeveelheid20;
    int hoeveelheid50;
    String pagina; //Elk panel binden aan een pagina

    //First panel
    JLabel title = new JLabel("Welkom bij Mesosbank, Scan uw kaart");
    JButton scan = new JButton("Scan uw kaart");
    JButton sluitprogramma = new JButton("Exit program");
    static JPanel mainPanel = new JPanel();
    static JPanel buttonPanel = new JPanel(new GridBagLayout());
    static JPanel titlePanel = new JPanel();
    Font typecharacters = new Font("Times New Roman", Font.BOLD, 30);

    // ok Panel
    JButton okButton = new JButton("Ok [A]");
    JButton homeButton = new JButton("Exit [D]");
    static JPanel buttonokpanel = new JPanel(new GridBagLayout());
    static JPanel oktopPanel = new JPanel();

    //second panel Menu
    static JPanel topPanel = new JPanel();
    static JPanel centerPanel = new JPanel();
    static JPanel westPanel = new JPanel();
    static JPanel eastPanel = new JPanel();
    static JPanel bottomPanel = new JPanel();
    JButton exit = new JButton("Exit[D]");
    JButton returnButton = new JButton("Return[C]");
    JButton saldoButton = new JButton("Saldoweergeven[A]");
    JButton anderButton = new JButton("Ander Bedrag[B]");
    JButton tienEuro = new JButton("10 euro [1]");
    JButton twintigEuro = new JButton("20 euro [2]");
    JButton vijftigEuro = new JButton("50 euro[3]");

    //third panel saldo weergeven
    static JPanel saldoTop = new JPanel();
    static JPanel saldoCenter = new JPanel(new GridBagLayout());
    static JPanel saldoBottom = new JPanel();
    JButton exitSaldo = new JButton("Exit[D]");
    JButton returnSaldo = new JButton("Return[C]");
    JLabel huidigeSaldo = new JLabel();

    //fourth panel ander weergeven
    static JPanel anderTop = new JPanel();
    static JPanel anderCenter = new JPanel(new GridBagLayout());
    static JPanel anderBottom = new JPanel();
    JTextField anderField = new JTextField(10);

    JButton exitAnder = new JButton("Exit[D]");
    JButton returnAnder = new JButton("Return[C]");
    JButton enterAnder = new JButton("Bevestig[A]");
    JButton clearAnder = new JButton("Clear [*]");
    JButton backSpaceAnder = new JButton("Backspace [#]");


    //Fifth panel Bevestiging
    static JPanel bevestigTop = new JPanel();
    static JPanel bevestigCenter = new JPanel(new GridBagLayout());
    static JPanel bevestigBottom = new JPanel();
    JButton exitBevestig = new JButton("Exit[D]");
    JButton jaBevestig = new JButton("Ja[A]");
    JButton neeBevestig = new JButton("Nee[B]");

    //Sixth panel printbon
    static JPanel printTop = new JPanel();
    static JPanel printCenter = new JPanel(new GridBagLayout());
    JButton neeBon = new JButton("Nee[B]");
    JButton jaBon = new JButton("Ja[A]");

    // Seventh panel pin
    static JPanel pinTop = new JPanel();
    static JPanel pinBot = new JPanel();
    JPasswordField pinField = new JPasswordField(6);
    static JPanel pinCenter = new JPanel(new GridBagLayout());
    JButton enter = new JButton("Enter[A]");
    JButton backspace = new JButton("Backspace [#]");
    JButton clear = new JButton("Clear [*]");
    JButton exitPin = new JButton("Exit [D]");
    JLabel pinLabel = new JLabel("Voer uw pincode in");
    JButton hiddenButton = new JButton();

    //keuze
    JButton button10 = new JButton(" x 10 euro[1]");
    JButton button20 = new JButton(" x 20 euro[2]");
    JButton button50 = new JButton(" x 50 euro[3]");
    JButton returnKeuze = new JButton("Return[C]");
    JButton exitKeuze = new JButton("Exit[D]");

    ImageIcon logo = new ImageIcon("Mesoslogo.jpg");
    ImageIcon logoBevestig = new ImageIcon("Bevestigtemp.jpg");

    ImageIcon logoeind = new ImageIcon("Eindlogo.jpg");

    JLabel logoLabelhome = new JLabel(logo);
    JLabel logoLabelmenu = new JLabel(logo);

    JLabel logoLabelok = new JLabel(logoBevestig);

    JLabel logoLabelEnd = new JLabel(logoeind);

    JLabel geldBevestiging = new JLabel(); //Tekst voor de hoeveelheid opnemen van de user
    long saldo; // saldo van de user
    long inputgeld; // variable voor input geld.
    int kansen; //aantal pin kansen
    int secondsAll; // timer voor elk panel
    int secondsPin; // timer voor pinnen
    boolean checkLimiet; //limiet voor saldo
    static String adress;
    timer timerCounter = new timer();


    private class timer
    {
        Timer timer = new Timer();
        TimerTask task = new TimerTask()
        {
            public void run()
            {
                secondsAll++;
                secondsPin++;
                if(secondsPin >60 && pagina == "pin")
                {
                    kansen = kansen + 1;
                    System.out.println(kansen);
                    secondsPin = 0;
                    if(kansen == 1 && pagina == "pin")
                    {
                        pinLabel.setText("U heeft nog 2 pogingen");
                    }
                    else if(kansen == 2 && pagina == "pin")
                    {
                        pinLabel.setText("U heeft nog 1 poging");
                    }
                    else if(kansen == 3 && pagina == "pin")
                    {
                        enter.setEnabled(false);
                        pinLabel.setText("Uw pas is geblokkeerd");
                        try {
                            TimeUnit.SECONDS.sleep(2);
                            cardTest.show(mainPanel, "home");
                            pagina = "home";
                            enter.setEnabled(true);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(secondsAll >60 && pagina != "pin")
                {
                    homeButton.doClick(100);
                    secondsAll = 0;
                }
                if(secondsAll> 8 && pagina == "eindPage")
                {
                    homeButton.doClick(100);
                    secondsAll = 0;
                }
            }
        };

        private void start()
        {
            timer.scheduleAtFixedRate(task, 1000, 1000);
        }
    }
//http://77.164.29.183
    //http://145.24.222.79:
    public ReadCard(int port) {
        client = ClientBuilder.newClient().register(JacksonFeature.class);
       // target =client.target("http://77.164.29.183");
        target = client.target(innerIP);
        readFrame();
        timerCounter.start();
    }

    static private Client client;
    private String outerIP = ("http://77.164.29.183");
    private String innerIP = ("http://145.24.222.79:"+ 8025);
    private WebTarget target;
    private String ID;
    private String uid;
    private  String iban;
    static WithdrawRequest request = new WithdrawRequest();
    static BalanceResponse balanceRequest = new BalanceResponse();
    static PinAuthenticatieResponse pinresponse = new PinAuthenticatieResponse();
    static AuthenticatieResponse validPas = new AuthenticatieResponse();
    static idresponse response = new idresponse();
    static long s;

    private void initialize() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }
        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);
            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            // open the streams!
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            inputStream = serialPort.getInputStream();
            output = serialPort.getOutputStream();
            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());

        }
    }

    private void nullmaker() {
        pinField.setText(null);
        anderField.setText(null);
    }

    SerialPort serialPort;
    /**
     * The port we're normally going to use.
     */
    private static final String PORT_NAMES[] = {
            // "/dev/tty.usbmodem1d11", // Mac OS X
            //"/dev/ttyUSB0", // Linux
            "COM2","COM3", "COM4", "COM5","COM6","COM0","COM1","COM7","COM8","COM9","COM10" // Windows
    };

    private BufferedReader input;
    /**
     * The output stream to the port
     */
    private OutputStream output;
    private InputStream inputStream;
    /**
     * Milliseconds to block while waiting for port open
     */
    private static final int TIME_OUT = 2000;

    private static final int DATA_RATE = 9600;

    public void readFrame() {

        pinField.setEditable(false);
        anderField.setEditable(false);
        initialize();
        JFrame mainFrame = new JFrame();
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setIconImage(logo.getImage());
        //mainFrame.setSize(new Dimension(1000,600));
        mainFrame.setUndecorated(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null); //De frame in de midden van de scherm plaatsen
        mainFrame.setVisible(true); // frame zichtbaar maken
        mainPanel.setLayout(cardTest); // in de main panel wordt de cardlayout gebruikt.

        GridBagConstraints gbc = new GridBagConstraints();
        scan.setFont(typecharacters);
        scan.setPreferredSize(new Dimension(400, 100));


        title.setFont(typecharacters);
        //titlePanel.add(title);
        gbc.gridx = 0;
        gbc.gridy = 50;
        buttonPanel.add(title,gbc);
        buttonPanel.add(logoLabelhome,gbc);

        JPanel homePag = new JPanel();
        homePag.setLayout(new BorderLayout());
        //homePag.add(titlePanel, BorderLayout.NORTH);
        homePag.add(buttonPanel, BorderLayout.CENTER);
        geldBevestiging.setFont(typecharacters);
/////////////////// main menu
        JPanel pMenu = new JPanel();
        pMenu.setLayout(new BorderLayout());
        JLabel titleMenu = new JLabel("Mesosbank mainmenu");

        titleMenu.setFont(typecharacters);
        JLabel snelGeld = new JLabel("Opties voor snelopnemen");
        final JLabel blankLabel = new JLabel(".");
        snelGeld.setFont(typecharacters);
        blankLabel.setFont(typecharacters);
        tienEuro.setPreferredSize(new Dimension(200, 50));
        tienEuro.setFont(typecharacters);
        twintigEuro.setPreferredSize(new Dimension(200, 50));
        twintigEuro.setFont(typecharacters);
        vijftigEuro.setPreferredSize(new Dimension(200, 50));
        vijftigEuro.setFont(typecharacters);
        saldoButton.setFont(typecharacters);
        anderButton.setFont(typecharacters);
        returnButton.setPreferredSize(new Dimension(200, 50));
        returnButton.setFont(typecharacters);
        exit.setPreferredSize(new Dimension(200, 50));
        exit.setFont(typecharacters);

        centerPanel.setLayout(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = 50;
        centerPanel.add(blankLabel,gbc);
        centerPanel.add(logoLabelmenu,gbc);
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.PAGE_AXIS));
        westPanel.add(snelGeld);
        westPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        westPanel.add(tienEuro);
        westPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        westPanel.add(twintigEuro);
        westPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        westPanel.add(vijftigEuro);

        topPanel.add(titleMenu);


        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.PAGE_AXIS));
        eastPanel.add(saldoButton);
        eastPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        eastPanel.add(anderButton);

        bottomPanel.add(returnButton);
        bottomPanel.add(exit);

        pMenu.add(westPanel, BorderLayout.WEST);
        pMenu.add(topPanel, BorderLayout.NORTH);
        pMenu.add(centerPanel, BorderLayout.CENTER);
        pMenu.add(eastPanel, BorderLayout.EAST);
        pMenu.add(bottomPanel, BorderLayout.SOUTH);
///////////saldo
        JPanel saldoPanel = new JPanel(); //panel met methode binden
        saldoPanel = saldoWeergave();
//////// ander
        JPanel anderePage = new JPanel();
        anderePage = andereBedrag();
////////bevestig
        JPanel bevestigPag = new JPanel();
        bevestigPag = bevestiging();
//// bon printen
        JPanel bonPag = new JPanel();
        bonPag = bonPrint();
///// pin
        JPanel pinPage = new JPanel();
        pinPage = pinMaken();

        JPanel okPage = new JPanel();
        okPage = okPanel();

        JPanel keuzePage = new JPanel();
        keuzePage = keuzeBiljet();

        JPanel eindPage = new JPanel();
        eindPage = eindeProces();
///
        cardTest.show(mainPanel,"home");
        mainPanel.add(homePag, "home");// mainpanel linken aan een ander panel en de benaming van dat panel
        mainPanel.add(okPage, "ok");
        mainPanel.add(pinPage, "pin");
        mainPanel.add(pMenu, "menu");
        mainPanel.add(saldoPanel, "saldoPage");
        mainPanel.add(anderePage, "anderPage");
        mainPanel.add(keuzePage,"keuze");
        mainPanel.add(bevestigPag, "bevestigPage");
        mainPanel.add(bonPag, "bonPage");
        mainPanel.add(eindPage, "eindPage");
        mainFrame.add(mainPanel);
        pagina = "home";
        //First panel
        scan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cardTest.show(mainPanel, "ok");
                pagina = "ok";
                pinField.setText("");
            }
        });
        sluitprogramma.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });

        //Second panel main
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cardTest.show(mainPanel, "home");
                pagina = "home";
                blankLabel.setText(".");
                blankLabel.setForeground(Color.BLACK);
                anderField.setText("");
                pinField.setText("");
            }
        });

        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cardTest.show(mainPanel, "home");
                anderField.setText("");
                pinField.setText("");
                blankLabel.setText(".");
                blankLabel.setForeground(Color.BLACK);
                pagina = "home";
            }
        });
        //Jbutton gelinked aan saldo weergave
        saldoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                BalanceResponse balanceResponse = balance(iban);
                saldo = balanceResponse.getBalans();
                huidigeSaldo.setText("Uw huidige saldo is " + saldo + " euro");

                cardTest.show(mainPanel, "saldoPage");
                pagina = "saldoPage";
                blankLabel.setText(".");
                blankLabel.setForeground(Color.BLACK);
            }
        });

        anderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cardTest.show(mainPanel, "anderPage");
                pagina = "anderPage";
                BalanceResponse balanceResponse = balance(iban);
                saldo = balanceResponse.getBalans();
                blankLabel.setText(".");
                blankLabel.setForeground(Color.BLACK);
                anderField.setText("");
                pinField.setText("");
            }
        });
        tienEuro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                inputgeld = 10;
                BalanceResponse balanceResponse = balance(iban);
                saldo = balanceResponse.getBalans();
                if(biljet10>0) {
                    tienEuro.setEnabled(true);
                    if (inputgeld > saldo) //Saldo controleren op genoeg saldo, als de gewenste bedrag is ingevoerd.
                    {
                        System.out.println("Werkt 10 euro");
                        blankLabel.setForeground(Color.red);
                        blankLabel.setText("Onvoldoende saldo.");
                    } else if (inputgeld < saldo || inputgeld == saldo) {
                        cardTest.show(mainPanel, "bevestigPage");
                        checkLimiet = true;
                        geldBevestiging.setText("Wilt u " + inputgeld + " euro opnemen?");
                        blankLabel.setText(".");
                        blankLabel.setForeground(Color.BLACK);
                        hoeveelheid10 = 1;
                        pagina = "bevestigPage";
                    }
                }
                else
                {
                tienEuro.setEnabled(false);
                }

            }
        });
        twintigEuro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                inputgeld = 20;
                BalanceResponse balanceResponse = balance(iban);
                saldo = balanceResponse.getBalans();
                if(biljet20 >0) {
                    twintigEuro.setEnabled(true);
                    if (inputgeld > saldo) {
                        System.out.println("Werkt20 euro");
                        blankLabel.setForeground(Color.red);
                        blankLabel.setText("Onvoldoende saldo.");
                    } else if (inputgeld < saldo || inputgeld == saldo) {
                        cardTest.show(mainPanel, "bevestigPage");
                        checkLimiet = true;
                        geldBevestiging.setText("Wilt u " + inputgeld + " euro opnemen?");
                        blankLabel.setText(".");
                        hoeveelheid20 = 1;
                        blankLabel.setForeground(Color.BLACK);
                        pagina = "bevestigPage";
                    }
                }
                else
                {
                    twintigEuro.setEnabled(false);
                }
            }

        });
        vijftigEuro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                inputgeld = 50;
                BalanceResponse balanceResponse = balance(iban);
                saldo = balanceResponse.getBalans();
                if(biljet50>0) {
                    vijftigEuro.setEnabled(true);
                    if (inputgeld > saldo) {

                        System.out.println("Werkt 50 euro");
                        blankLabel.setForeground(Color.red);
                        blankLabel.setText("Onvoldoende saldo.");
                    } else if (inputgeld < saldo || inputgeld == saldo) {
                        cardTest.show(mainPanel, "bevestigPage");
                        checkLimiet = true;
                        geldBevestiging.setText("Wilt u " + inputgeld + " euro opnemen?");
                        blankLabel.setText(".");
                        hoeveelheid50 = 1;
                        blankLabel.setForeground(Color.BLACK);
                        pagina = "bevestigPage";
                    }
                }
                else
                {
                    vijftigEuro.setEnabled(false);
                }
            }
        });

        return;
    }

    private JPanel okPanel()
    {
        JPanel okPanel = new JPanel();
        okPanel.setLayout(new BorderLayout());
        JLabel okLabel = new JLabel("Bevestig uw kaart");
        GridBagConstraints gbc = new GridBagConstraints();

        buttonokpanel.setFont(typecharacters);
        okLabel.setFont(typecharacters);
        okButton.setFont(typecharacters);
        okButton.setPreferredSize(new Dimension(200, 100));
        homeButton.setFont(typecharacters);
        homeButton.setPreferredSize(new Dimension(200, 100));

        buttonokpanel.add(logoLabelok,gbc);

        okPanel.add(buttonokpanel,BorderLayout.CENTER);
        okPanel.add(oktopPanel, BorderLayout.NORTH);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                {
                    cardTest.show(mainPanel, "pin");
                    pagina = "pin";
                    pinField.setText("");

                }
            }
        });

        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                {
                    cardTest.show(mainPanel, "home");
                    pagina = "home";
                    pinField.setText("");

                }
            }
        });

        return okPanel;
    }
    private JPanel pinMaken() {
        JPanel pM = new JPanel();
        pM.setLayout(new BorderLayout());
        pinLabel.setFont(typecharacters);
        GridBagConstraints gbc = new GridBagConstraints();
        enter.setFont(typecharacters);
        backspace.setFont(typecharacters);
        clear.setFont(typecharacters);
        enter.setPreferredSize(new Dimension(200, 50));
        pinField.setFont(typecharacters);
        pinField.setPreferredSize(new Dimension(200, 50));
        pinField.setHorizontalAlignment(SwingConstants.CENTER);

        exitPin.setPreferredSize(new Dimension(200, 50));
        exitPin.setFont(typecharacters);

        gbc.gridx = 150;
        gbc.gridy = 0;
        pinCenter.add(pinLabel,gbc);
        gbc.gridx = 0;
        gbc.gridy = 200;
        pinCenter.add(enter, gbc);
        gbc.gridx = 150;
        gbc.gridy = 200;
        pinCenter.add(backspace, gbc);
        gbc.gridx = 300;
        gbc.gridy = 200;
        pinCenter.add(clear, gbc);
        gbc.gridx = 150;
        gbc.gridy = 100;
        pinCenter.add(pinField, gbc);

        pinBot.add(exitPin);
        pM.add(pinBot, BorderLayout.SOUTH);
        pM.add(pinTop, BorderLayout.NORTH);
        pM.add(pinCenter);

        /// Seventh panel pin, controle voor pincode
        enter.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae) {
                {
                    PinAuthenticatieResponse pinAuthenticatieResponse = pinAuthenticatie(uid, String.valueOf(pinField.getPassword()));
                    System.out.println();
                    if(pinAuthenticatieResponse.isPin() == true) {
                        cardTest.show(mainPanel, "menu");
                        pagina = "menu";
                        System.out.println("Toegang ");

                        pinLabel.setText("Voer uw pincode in");
                        anderField.setText("0");
                    }
                    else if(pinAuthenticatieResponse.isPin()  == false && pinAuthenticatieResponse.getFailedAttemps()<=2)
                    {

                        pinLabel.setText("U heeft nog "+(3-pinAuthenticatieResponse.getFailedAttemps())+" poging(en)");
                    }

                    else if(pinAuthenticatieResponse.getGeblokeerdpas() == 1)
                    {
                        enter.setEnabled(false);
                        pinLabel.setText("Uw pas is geblokkeerd");
                        try {
                            TimeUnit.SECONDS.sleep(2);
                            cardTest.show(mainPanel, "home");
                            pagina = "home";
                            enter.setEnabled(true);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
        exitPin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                {
                    cardTest.show(mainPanel, "home");
                    pagina = "home";
                    pinLabel.setText("Voer uw pincode in");

                }
            }
        });
        return pM;
    }

    private JPanel bonPrint() {
        JPanel bonPage = new JPanel();
        bonPage.setLayout(new BorderLayout());
        JLabel titleBon = new JLabel("Mesosbank bonnenprinter");
        JLabel vraagBon = new JLabel("Wilt u een bon?");
        GridBagConstraints gbc = new GridBagConstraints();
        printTop.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleBon.setFont(typecharacters);
        vraagBon.setFont(typecharacters);
        jaBon.setFont(typecharacters);
        jaBon.setPreferredSize(new Dimension(200, 50));
        neeBon.setFont(typecharacters);
        neeBon.setPreferredSize(new Dimension(200, 50));

        printTop.add(titleBon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        printCenter.add(vraagBon, gbc);
        gbc.gridx = 0;
        gbc.gridy = 200;
        printCenter.add(jaBon, gbc);
        gbc.gridx = 5;
        gbc.gridy = 200;
        printCenter.add(neeBon, gbc);
        bonPage.add(printTop, BorderLayout.NORTH);
        bonPage.add(printCenter, BorderLayout.CENTER);
        ///////////////// Sixth panel print bon
        jaBon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cardTest.show(mainPanel, "eindPage");
                anderField.setText("");
                pinField.setText("");
                pagina = "eindPage";
            }
        });
        neeBon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cardTest.show(mainPanel, "eindPage");
                anderField.setText("");
                pinField.setText("");
                pagina = "eindPage";
            }
        });
        return bonPage;
    }

    private JPanel bevestiging() {
        JPanel bevestigPage = new JPanel();
        bevestigPage.setLayout(new BorderLayout());
        JLabel titleBevestig = new JLabel("Mesosbank bevestiging");
        GridBagConstraints gbc = new GridBagConstraints();
        titleBevestig.setFont(typecharacters);
        jaBevestig.setFont(typecharacters);
        jaBevestig.setPreferredSize(new Dimension(200, 50));
        neeBevestig.setFont(typecharacters);
        neeBevestig.setPreferredSize(new Dimension(200, 50));
        exitBevestig.setPreferredSize(new Dimension(200, 50));
        exitBevestig.setFont(typecharacters);

        bevestigTop.add(titleBevestig);

        gbc.gridx = 0;
        gbc.gridy = 0;
        bevestigCenter.add(geldBevestiging, gbc);
        gbc.gridx = 0;
        gbc.gridy = 200;
        bevestigCenter.add(jaBevestig, gbc);
        gbc.gridx = 5;
        gbc.gridy = 200;
        bevestigCenter.add(neeBevestig, gbc);
        bevestigBottom.add(exitBevestig);
        bevestigPage.add(bevestigTop, BorderLayout.NORTH);
        bevestigPage.add(bevestigCenter);
        bevestigPage.add(bevestigBottom, BorderLayout.SOUTH);
        /////// fifth panel bevestigPage
        exitBevestig.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cardTest.show(mainPanel, "home");
                pagina = "home";
                geldBevestiging.setForeground(Color.black);
            }
        });

        neeBevestig.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cardTest.show(mainPanel, "menu");
                pagina = "menu";
                geldBevestiging.setForeground(Color.black);
            }
        });

        jaBevestig.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                if(checkLimiet() == false)
                {
                    cardTest.show(mainPanel, "bonPage");
                    pagina = "bonPage";
                    System.out.println("Geen limiet");
                    WithdrawResponse response = withdraw(iban, inputgeld);
                    biljet10 = biljet10-hoeveelheid10;
                    System.out.println(biljet10);
                    biljet20 = biljet20-hoeveelheid20;
                    System.out.println(biljet20);
                    biljet50 = biljet50-hoeveelheid50;
                    System.out.println(biljet50);
                }
                else if(checkLimiet() == true)
                {

                    geldBevestiging.setText("Limiet bereik!");
                    geldBevestiging.setForeground(Color.red);
                    System.out.println("limiet bereikt!");
                }
            }
        });
        return bevestigPage;
    }

    private JPanel andereBedrag() {
        JPanel anderPage = new JPanel();
        JLabel titleAnder = new JLabel("Mesosbank custom bedrag");
        JLabel textAnder = new JLabel("Voer uw bedrag in:");
        final JLabel textVoorbeeld = new JLabel("Alleen tientallen invoeren");
        textAnder.setFont(typecharacters);
        titleAnder.setFont(typecharacters);
        enterAnder.setFont(typecharacters);
        anderField.setFont(typecharacters);
        backSpaceAnder.setFont(typecharacters);
        clearAnder.setFont(typecharacters);
        textVoorbeeld.setFont(typecharacters);
        returnAnder.setPreferredSize(new Dimension(200, 50));
        returnAnder.setFont(typecharacters);
        exitAnder.setPreferredSize(new Dimension(200, 50));
        exitAnder.setFont(typecharacters);

        anderPage.setLayout(new BorderLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        anderField.setHorizontalAlignment(SwingConstants.CENTER);
        anderTop.add(titleAnder);
        gbc.gridx = 150;
        gbc.gridy = 0;
        anderCenter.add(textAnder, gbc);
        gbc.gridx = 150;
        gbc.gridy = 50;
        anderCenter.add(textVoorbeeld, gbc);
        gbc.gridx = 150;
        gbc.gridy = 100;
        anderCenter.add(anderField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 300;
        anderCenter.add(enterAnder, gbc);
        gbc.gridx = 150;
        gbc.gridy = 300;
        anderCenter.add(backSpaceAnder, gbc);
        gbc.gridx = 300;
        gbc.gridy = 300;
        anderCenter.add(clearAnder, gbc);
        anderBottom.add(returnAnder);
        anderBottom.add(exitAnder);
        anderPage.add(anderTop, BorderLayout.NORTH);
        anderPage.add(anderCenter);
        anderPage.add(anderBottom, BorderLayout.SOUTH);
        /////////////// fourth panel ander bedrag

        enterAnder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                checkLimiet();
                checkSaldo();
                if(checkEental() == false) //controle voor eenheden bij ander bedragen
                {
                    textVoorbeeld.setForeground(Color.RED);
                    textVoorbeeld.setText("Alleen tientallen invoeren!");

                }
                else if(checkLimiet() == true) //controle voor eenheden bij ander bedragen
                {
                    textVoorbeeld.setForeground(Color.RED);
                    textVoorbeeld.setText("Limiet bereikt!");

                }
                else if(checkEental() == true)
                {
                    if(checkSaldo() == true)
                    {
                        textVoorbeeld.setForeground(Color.RED);
                        textVoorbeeld.setText("Onvoldoende saldo, check uw saldo A.U.B.");
                    }
                    else if(checkSaldo() == false && checkLimiet() == false)
                    {
                        inputgeld = Integer.parseInt(anderField.getText());
                        cardTest.show(mainPanel, "keuze");
                        textVoorbeeld.setText("Alleen tientallen invoeren");
                        textVoorbeeld.setForeground(Color.black);
                        pagina = "keuze";
                        checkHoeveelheidBiljetten();
                        checkBiljet();
                    }
                }
                else if(geldBevestiging.getText() == null)
                {
                    System.out.println("No input");

                }
            }
        });

        exitAnder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cardTest.show(mainPanel, "home");
                anderField.setText("0");
                inputgeld = 0;
                pagina = "home";
                textVoorbeeld.setText("Alleen tientallen invoeren");
                textVoorbeeld.setForeground(Color.black);
            }
        });

        returnAnder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cardTest.show(mainPanel, "menu");
                anderField.setText("0");
                inputgeld = 0;
                pagina = "menu";
                textVoorbeeld.setText("Alleen tientallen invoeren");
                textVoorbeeld.setForeground(Color.black);
            }
        });
        return anderPage;
    }

    private JPanel keuzeBiljet() {

        JLabel textKeuze = new JLabel("Kies uw gewenste biljetten");
        textKeuze.setFont(typecharacters);
        JPanel keuzePanel = new JPanel();
        JPanel keuzeMid = new JPanel();
        JPanel keuzeEast = new JPanel();
        JPanel keuzeSouth = new JPanel();
        JPanel keuzeNorth = new JPanel();
        button10.setFont(typecharacters);
        button20.setFont(typecharacters);
        button50.setFont(typecharacters);
        JLabel keuzeLabel = new JLabel("Mesosbank");
        keuzeLabel.setFont(typecharacters);
        exitKeuze.setFont(typecharacters);
        returnKeuze.setFont(typecharacters);
        keuzePanel.setLayout(new BorderLayout());

        keuzeNorth.add(keuzeLabel);
        keuzeSouth.add(returnKeuze);
        keuzeSouth.add(exitKeuze);

        keuzeEast.setLayout(new BoxLayout(keuzeEast, BoxLayout.PAGE_AXIS));
        keuzeEast.add(Box.createRigidArea(new Dimension(0, 5)));
        keuzeEast.add(textKeuze);
        keuzeEast.add(Box.createRigidArea(new Dimension(0, 5)));
        keuzeEast.add(button10);
        keuzeEast.add(Box.createRigidArea(new Dimension(0, 5)));
        keuzeEast.add(button20);
        keuzeEast.add(Box.createRigidArea(new Dimension(0, 5)));
        keuzeEast.add(button50);

        keuzePanel.add(keuzeNorth,BorderLayout.NORTH);
        keuzePanel.add(keuzeMid, BorderLayout.CENTER);
        keuzePanel.add(keuzeEast,BorderLayout.EAST);
        keuzePanel.add(keuzeSouth,BorderLayout.SOUTH);
        pagina = "keuze";

        button10.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                cardTest.show(mainPanel, "bevestigPage");
                button50.setEnabled(true);
                geldBevestiging.setText("Wilt u " + anderField.getText() + " euro opnemen?");
                pagina = "bevestigPage";
            }
        });
        button20.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                cardTest.show(mainPanel, "bevestigPage");
                button50.setEnabled(true);
                geldBevestiging.setText("Wilt u " + anderField.getText() + " euro opnemen?");
                pagina = "bevestigPage";
            }
        });
        button50.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                cardTest.show(mainPanel, "bevestigPage");
                geldBevestiging.setText("Wilt u " + anderField.getText() + " euro opnemen?");
                pagina = "bevestigPage";
            }
        });

        returnKeuze.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cardTest.show(mainPanel, "menu");
                anderField.setText("0");
                inputgeld = 0;
                button50.setEnabled(true);
                pagina = "menu";
            }
        });

        exitKeuze.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cardTest.show(mainPanel, "home");
                anderField.setText("0");
                inputgeld = 0;
                button50.setEnabled(true);
                pagina = "home";

            }
        });

        return keuzePanel;
    }

    private JPanel eindeProces()
    {
        JPanel eindPanel = new JPanel();
        eindPanel.setLayout(new GridBagLayout());
        JPanel eindImage = new JPanel();
        JPanel filler = new JPanel();
        JButton eindbutton = new JButton();

        GridBagConstraints gbc = new GridBagConstraints();

        eindImage.add(logoLabelEnd,gbc);
        eindPanel.add(eindImage);
        pagina = "eindPage";

        eindbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cardTest.show(mainPanel, "home");
                anderField.setText("0");
                inputgeld = 0;
                pagina = "home";

            }
        });
        return eindPanel;
    }

    private JPanel saldoWeergave() {
        JPanel saldoPage = new JPanel();
        JLabel titleSaldo = new JLabel("Mesosbank saldoweergave");
        titleSaldo.setFont(typecharacters);
        GridBagConstraints gbc = new GridBagConstraints();
        huidigeSaldo.setFont(typecharacters);
        returnSaldo.setPreferredSize(new Dimension(200, 50));
        returnSaldo.setFont(typecharacters);
        exitSaldo.setPreferredSize(new Dimension(200, 50));
        exitSaldo.setFont(typecharacters);

        saldoCenter.add(huidigeSaldo, gbc);
        saldoPage.setLayout(new BorderLayout());
        saldoTop.add(titleSaldo);
        saldoBottom.add(returnSaldo);
        saldoBottom.add(exitSaldo);
        saldoPage.add(saldoCenter);
        saldoPage.add(saldoTop, BorderLayout.NORTH);
        saldoPage.add(saldoBottom, BorderLayout.SOUTH);

        //Third panel Saldo
        exitSaldo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cardTest.show(mainPanel, "home");
                anderField.setText("");
                pagina = "home";
            }
        });

        returnSaldo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cardTest.show(mainPanel, "menu");
                pagina = "menu";
            }
        });
        return saldoPage;
    }

    private void checkHoeveelheidBiljetten() {
        int getal = Integer.parseInt(anderField.getText());
        int amount10, amount20, amount50, amount20a,amount20b;
        amount10 = getal / 10;
        amount20 = getal / 20;
        amount50 = getal / 50;

        if (amount10 > biljet10 || biljet10 == 0)
        {
            System.out.println("Geen tiennen over!");
            button10.setEnabled(false);

            if (amount20 > biljet20 || biljet20 == 0)
            {
                button20.setEnabled(false);
                if (amount50 > biljet50||biljet50 == 0) {
                    button50.setEnabled(false);
                }
            }
                 if (amount50 > biljet50|| biljet50 == 0)
                {
                    button50.setEnabled(false);
                    if (amount20 > biljet20||biljet20 == 0)
                    {
                        button20.setEnabled(false);
                    }
                }
        }

        if (amount20 > biljet20 || biljet20 ==0)
        {
            System.out.println("Geen twintigjes over!");
            button20.setEnabled(false);
            amount20a = getal - (50*amount50);
            amount20b = amount20a/20;
            System.out.println(amount20b);
            if (amount10 > biljet10|| biljet10 ==0)
            {
                button10.setEnabled(false);
                button50.setEnabled(false);
                if (amount50 > biljet50|| biljet50 ==0)
                {
                    button50.setEnabled(false);
                }
            }
             if (amount50 > biljet50|| biljet50 ==0)
            {
                button20.setEnabled(false);
                button50.setEnabled(false);
                if (amount10 > biljet10|| biljet10 ==0)
                {
                    button10.setEnabled(false);
                }
            }
            if(amount20b>0)
            {
                button50.setEnabled(false);
            }

        }

         if (amount50 > biljet50|| biljet50 ==0)
        {
            System.out.println("Geen vijftigjes over!");
            button50.setEnabled(false);
            if (amount10 > biljet10|| biljet10 ==0)
            {
                button10.setEnabled(false);
                if (amount20 > biljet20|| biljet20 ==0) {
                    button20.setEnabled(false);
                }
            }
             if (amount20 > biljet20|| biljet20 ==0)
            {
                button20.setEnabled(false);
                if (amount10 > biljet10|| biljet10 ==0)
                {
                    button10.setEnabled(false);
                }
            }
        }
        else
        {
            if(biljet10 >= amount10) {
                button10.setEnabled(true);
                if(biljet20 >= amount20)
                {
                    button20.setEnabled(true);
                }
                if(biljet50 >=amount50)
                {
                    button50.setEnabled(true);
                }
            }
            if(biljet20>= amount20) {
                button20.setEnabled(true);
                if(biljet10 >= amount10)
                {
                    button10.setEnabled(true);
                }
                if(biljet50>=amount50)
                {
                    button50.setEnabled(true);
                }
            }
            if(biljet50 >= amount50)
            {
                button50.setEnabled(true);
                if(biljet10>amount10)
                {
                    button10.setEnabled(true);
                }
                if(biljet20>=amount20)
                {
                    button20.setEnabled(true);
                }
            }
        }

                return;
            }

    private int checkBiljet()
    {
        int getal = Integer.parseInt(anderField.getText());
        int firstcheck10, secondcheck10, thirdcheck10, fourthcheck10;
        int firstcheck20, secondcheck20;
        int firstcheck50;
        firstcheck10 = getal/10;
        firstcheck20 = getal/20;
        firstcheck50 = getal/50;
       int remaining50;
       int remaining20;
       int remaining10;
       int remainingsaldo, secondremainingsaldo, thirdremainingsaldo;

        if(firstcheck20>=1)
        {
            secondremainingsaldo = getal-firstcheck20* 20;
            fourthcheck10 = secondremainingsaldo /10;
            if(firstcheck50>0) {

                remaining50 = firstcheck50 * 50;
                remainingsaldo = getal - remaining50;
                secondcheck20 = remainingsaldo / 20;
                secondcheck10 = remainingsaldo / 10;

                if (secondcheck20 >= 1) {
                    thirdcheck10 = remainingsaldo - (secondcheck20 * 20);
                    if (thirdcheck10 >= 1) {
                        button50.setText(String.valueOf(thirdcheck10 + "x €10 " + secondcheck20 + "x €20 " + firstcheck50 + "x €50 [3]"));
                        hoeveelheid10 = thirdcheck10;
                        hoeveelheid20 = secondcheck20;
                        hoeveelheid50 = firstcheck50;
                    }
                    else
                    {
                        button50.setText(String.valueOf(secondcheck20 + "x €20 " + firstcheck50 + "x €50[3]"));
                        hoeveelheid20 = secondcheck20;
                        hoeveelheid50 = firstcheck50;
                    }
                    button20.setText(String.valueOf(fourthcheck10+"x €10 "+ firstcheck20+"x €20[2]"));
                    hoeveelheid10 = fourthcheck10;
                    hoeveelheid20 = firstcheck20;
                } else if (secondcheck10 >= 1) {
                    button50.setText(String.valueOf(secondcheck10 + "x €10 " + firstcheck50 + "x €50[3]"));
                    hoeveelheid10 = secondcheck10;
                    hoeveelheid50 = firstcheck50;
                    if(fourthcheck10 == 0)
                    {
                        button20.setText(String.valueOf(firstcheck20 + "x €20[2]"));
                        hoeveelheid20=firstcheck20;
                    }
                    else if(fourthcheck10>=1)
                    {
                        button20.setText(String.valueOf(fourthcheck10+"x €10 "+firstcheck20+"x €20[2]"));
                        hoeveelheid10 = fourthcheck10;
                        hoeveelheid20 = firstcheck20;
                    }
                } else {
                    button50.setText(String.valueOf(firstcheck50 + "x €50 [3]"));
                    hoeveelheid50 = firstcheck50;
                }
            }
            else
            {
                button50.setEnabled(false);
            }
            if(fourthcheck10>=0)
            {
                secondcheck10 = secondremainingsaldo / 10;
                button20.setText(String.valueOf(secondcheck10 + "x €10 " + firstcheck20 + "x €20[2]"));
                hoeveelheid10 = secondcheck10;
                hoeveelheid20 = firstcheck20;
            }
            button20.setText(String.valueOf(firstcheck20+"x €20 [2]"));
            hoeveelheid20 = firstcheck20;

        }
        else
        {
            button20.setEnabled(false);
            button50.setEnabled(false);
        }
        button10.setText(String.valueOf(firstcheck10+"x €10[1]"));
        hoeveelheid10 = firstcheck10;

        return checkBiljet();
    }

    private boolean checkEental()
    {
        int getal = Integer.parseInt(anderField.getText());
        int remainderEental = 10;
        int eental = getal % remainderEental;
        boolean checkEental;

        if(eental == 0 && getal >0)
        {
            System.out.println("Eental systeem");
            checkEental = true;
            return checkEental;
        }
        else {
            System.out.println("niet goed.");
            checkEental = false;
            return checkEental;
        }

    }

    private boolean checkSaldo()
    {
        int getal = Integer.parseInt(anderField.getText());
        boolean checkSaldo;
        BalanceResponse balanceResponse = balance(iban);
        saldo = balanceResponse.getBalans();
        if(getal> saldo)
        {
            System.out.println("Niet genoeg saldo werkt");
            checkSaldo = true;
            return checkSaldo;
        }
        else
        {
            checkSaldo = false;
            System.out.println("Genoeg saldo");
            return checkSaldo;
        }

    }

    private boolean checkLimiet()
    {
        int ditLimiet = 500;
        int getal = Integer.parseInt(anderField.getText());

        if(getal>0)
        {
            if(getal >ditLimiet)
            {
                System.out.println("limiet");
                checkLimiet = true;
                return checkLimiet;
            }
            else
            {
                System.out.println("buiten werkt");
                checkLimiet = false;
                return checkLimiet;
            }
        }
        else
        {
            checkLimiet = false;
            return checkLimiet;
        }

    }

    int switchingcount= 0;
    ///////////////////// serialprinlnt + input van button een functie toewijzen.
    public void serialEvent(SerialPortEvent spe) {
        if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE)

            try
            {
                byte[] readBuffer = new byte[40];
                String inputLine = input.readLine();
                System.out.println(inputLine);

                //home scan page
                if(pagina == "home")
                {

                    String UID = inputLine.substring(0, 8);
                    uid = UID;
                    System.out.println(UID);

                    ID = (hexToAscii(inputLine.substring(8,30)));
                    iban = ID;
                    System.out.println(ID);

                    boolean checkID = authenticatie(UID,ID);

                    if (inputLine.equals("D")) {
                        sluitprogramma.doClick(400);
                        System.out.println("close");
                    }
                   else if (checkID == true )
                    {
                        title.setText("Welkom bij Mesosbank, Scan uw kaart)");
                        scan.doClick(300);
                        System.out.println(" scan worked");
                        secondsAll = 0;inputLine = null;
                        return;
                    }
                    else if(checkID == false)
                    {
                        title.setText("Rescan uw pas/Onbekende pas");
                        if(switchingcount ==0)
                        {
                            target = client.target(outerIP);
                        }
                        if(switchingcount ==1)
                        {
                            target = client.target(innerIP);
                        }
                        if(switchingcount ==2)
                        {
                            target = client.target(outerIP);
                            switchingcount =0;
                        }
                         else
                         {
                            switchingcount++;
                             System.out.println("Wrong IP");
                         }
                    }

                }
                // ok page
                if(pagina == "ok") {
                    if (inputLine.equals("A")) {
                        okButton.doClick(300);inputLine = null;}
                    else if (inputLine.equals("D")) {homeButton.doClick(400);}
                }
                //pin invoeren
                if(pagina == "pin")
                {
                    if(inputLine.equals("A")){enter.doClick(1000); inputLine = null;}
                    else if (inputLine.equals("D")) {exit.doClick(300);}
                    else if (inputLine.equals("B")) {inputLine = null;}
                    else if (inputLine.equals("C")) {inputLine = null;}
                    else if (inputLine.equals("*")) nullmaker();
                    else if (pinField.getText().length() > 3)
                    {if (inputLine.equals("*")) nullmaker();
                    else if (inputLine.equals("#")) pinField.setText(pinField.getText().substring(0, pinField.getText().length() - 1));}
                    else if (inputLine.equals("#")) {pinField.setText(pinField.getText().substring(0, pinField.getText().length() - 1));}
                    else {pinField.setText(pinField.getText() + inputLine);}
                }

                //menu
                if(pagina == "menu")
                {
                    if (inputLine.equals("1")) {tienEuro.doClick(300);
                    } else if (inputLine.equals("2")) {twintigEuro.doClick(300);
                    } else if (inputLine.equals("3")) {vijftigEuro.doClick(300);
                    } else if (inputLine.equals("A") ) {saldoButton.doClick(300);inputLine = null;
                    } else if (inputLine.equals("B")) {anderButton.doClick(300);inputLine = null;
                    } else if (inputLine.equals("D")) {exit.doClick(300);}
                    else if (inputLine.equals("C")) {returnButton.doClick(300);}
                }

                //Bevestig
                if(pagina == "bevestigPage")
                {
                    if (inputLine.equals("A")) {jaBevestig.doClick(300);inputLine = null;}
                    else if (inputLine.equals("B")) {neeBevestig.doClick(300);inputLine = null;}
                    else if (inputLine.equals("D")) {exitBevestig.doClick(300);}
                }

                if(pagina == "keuze")
                {
                    if (inputLine.equals("1")) {button10.doClick(300);}
                    else if(inputLine.equals("2")) {button20.doClick(300);}
                    else if(inputLine.equals("3")) {button50.doClick(300);}
                    else if(inputLine.equals("C")) {returnKeuze.doClick(300);}
                    else if (inputLine.equals("D")) {exitKeuze.doClick(300);}
                }
                //Saldo
                if(pagina == "saldoPage")
                {
                    if (inputLine.equals("C")) {returnSaldo.doClick(300);}
                    else if (inputLine.equals("D")) {exitSaldo.doClick(300);}
                }

                //andere saldo
                if(pagina == "anderPage") {
                    if (anderField.getText().length() > 5) {
                        anderField.setText("");
                    } else if (inputLine.equals("C")) {
                        returnAnder.doClick(300);
                    } else if (inputLine.equals("D")) {
                        exitAnder.doClick(300);
                    } else if (inputLine.equals("A")) {
                        enterAnder.doClick(300);inputLine = null;
                    } else if (inputLine.equals("*")) nullmaker();
                    else if (anderField.getText().length() > 3) {
                        if (inputLine.equals("*")) {nullmaker();}
                        else if (inputLine.equals("#")) {anderField.setText(anderField.getText().substring(0, anderField.getText().length() - 1));}}
                    else if (inputLine.equals("#")) {anderField.setText(anderField.getText().substring(0, anderField.getText().length() - 1));}
                    else if (inputLine.equals("B")) {inputLine = null;}
                    else{ anderField.setText(anderField.getText() + inputLine);}
                }

                //bon page
                if(pagina == "bonPage")
                {
                    if (inputLine.equals("A") && pagina == "bonPage") {jaBon.doClick(300);inputLine = null;}
                    else if (inputLine.equals("B") && pagina == "bonPage") {neeBon.doClick(300);inputLine = null;}
                }

                if(pagina != "ok")
                {
                    if(inputLine.equals("A")||inputLine.equals("C")||inputLine.equals("B")||inputLine.equals("1")||inputLine.equals("2")||inputLine.equals("3")||inputLine.equals("4")||inputLine.equals("5")
                            ||inputLine.equals("6")||inputLine.equals("7")||inputLine.equals("8")||inputLine.equals("9")||inputLine.equals("0")||inputLine.equals("#")||inputLine.equals("*"))
                    {secondsAll = 0;}
                }

            } catch (IOException e) {
                System.err.println(e.toString());
            }
    }

    public static String hexToAscii(String h){
        String hex = h;
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i+=2) {
            String str = hex.substring(i, i+2);
            output.append((char)Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    public BalanceResponse balance(String IBAN) {

        BalanceResponse response = target
                .path("/balance/" + IBAN)
                .request(MediaType.APPLICATION_JSON)
                .get(BalanceResponse.class);



        return response;
    }

    public WithdrawResponse withdraw(String iban, Long amount) {

        WithdrawRequest request = new WithdrawRequest();
        request.setAmount(amount);
        request.setIBAN(iban);

        WithdrawResponse response = target
                .path("/withdraw")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON), WithdrawResponse.class);

        return response;
    }

    public boolean authenticatie(String uid, String iban) {
        AuthenticatieRequest request = new AuthenticatieRequest();
                request.setIban(iban);
                request.setUid(uid);

        AuthenticatieResponse response = target
                .path("/verifycard").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON), AuthenticatieResponse.class);

        return response.isCardExists();
    }

    public PinAuthenticatieResponse pinAuthenticatie(String uid, String pin) {
        PinAuthenticatieRequest request = new PinAuthenticatieRequest();
        request.setPin(pin);
        request.setUid(uid);

        PinAuthenticatieResponse response = target
                .path("/verifypin").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON), PinAuthenticatieResponse.class);

        return response;
    }
}
