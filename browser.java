import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.scene.web.*;
import javafx.print.PrinterJob;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.*;


import static javafx.concurrent.Worker.State.FAILED;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class browser extends JFrame {

  private final JFXPanel jfxPanel = new JFXPanel();
  private WebEngine engine;

  private final JPanel panel = new JPanel(new BorderLayout());
  private final JLabel lblStatus = new JLabel();
  public JPanel con = new JPanel(new BorderLayout(5, 0));
  JButton bc = new JButton("<-");
  JButton df = new JButton("->");
  JButton refr = new JButton("🔁");
  JButton more = new JButton("History");


  private final JButton btnGo = new JButton("Go");
  private final JButton btnPrint = new JButton("Print");
  private final JTextField txtURL = new JTextField();
  private final JProgressBar progressBar = new JProgressBar();
  public JPanel Pan() {
    return panel;
  }
  public browser() {
    super();

    initComponents();
  }


  private void initComponents() {
    createScene();
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } 
    catch (ClassNotFoundException e) {
      e.printStackTrace();
    } 
    catch (InstantiationException e) {
      e.printStackTrace();
    } 
    catch (IllegalAccessException e) {
      e.printStackTrace();
    } 
    catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
    ActionListener al = new ActionListener() {
      @Override 
        public void actionPerformed(ActionEvent e) {
        loadURL(txtURL.getText());
      }
    };


    btnGo.addActionListener(al);
    txtURL.addActionListener(al);

    progressBar.setPreferredSize(new Dimension(150, 18));
    progressBar.setStringPainted(true);
    con.add(bc, BorderLayout.WEST);
    con.add(df, BorderLayout.CENTER);
    con.add(refr, BorderLayout.EAST);
    more.addActionListener(new ActionListener() {
      @Override
        public void actionPerformed(ActionEvent e) {
        Platform.runLater(new Runnable() {
          @Override
            public void run() {
            engine.load("file:///F:/History.html");
          }
        }
        );
      }
    }
    );
    refr.addActionListener(new ActionListener() {
      @Override
        public void actionPerformed(ActionEvent e) {
        Platform.runLater(new Runnable() {
          @Override
            public void run() {
            try {
              engine.reload();
            }
            catch (Exception e) {
            }
          }
        }
        );
      }
    }
    );
    df.addActionListener(new ActionListener() {
      @Override
        public void actionPerformed(ActionEvent e) {
        Platform.runLater(new Runnable() {
          @Override
            public void run() {
            try {
              engine.getHistory().go(1);
            }
            catch (Exception e) {
            }
          }
        }
        );
      };
    }
    );
    bc.addActionListener(new ActionListener() {
      @Override
        public void actionPerformed(ActionEvent e) {

        Platform.runLater(new Runnable() {
          @Override
            public void run() {
            try {
              engine.getHistory().go(-1);
            }
            catch (Exception e) {
            }
          }
        }
        );
      }
    }
    );


    JPanel topBar = new JPanel(new BorderLayout(5, 0));
    topBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
    topBar.add(txtURL, BorderLayout.CENTER);
    topBar.add(btnGo, BorderLayout.EAST);

    topBar.add(con, BorderLayout.WEST);
    topBar.add(txtURL, BorderLayout.CENTER);
    JPanel ppp = new JPanel(new BorderLayout(5, 0));
    ppp.add(more, BorderLayout.CENTER);
    topBar.add(ppp, BorderLayout.EAST);

    JPanel statusBar = new JPanel(new BorderLayout(5, 0));
    statusBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
    statusBar.add(lblStatus, BorderLayout.CENTER);
    statusBar.add(progressBar, BorderLayout.EAST);
    javax.swing.JButton jButton1;

    jButton1 = new javax.swing.JButton();

    jButton1.setText("Print");






    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
          engine.print(job);
          job.endJob();
        }
      }
    }
    );

    // Code adding the component to the parent container - not shown here

    statusBar.add(jButton1, BorderLayout.WEST);

    panel.add(topBar, BorderLayout.NORTH);
    panel.add(jfxPanel, BorderLayout.CENTER);
    panel.add(statusBar, BorderLayout.SOUTH);

    getContentPane().add(panel);

    setPreferredSize(new Dimension(1024, 600));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
  };

  private void createScene() {

    Platform.runLater(new Runnable() {
      @Override 
        public void run() {

        WebView view = new WebView();
        engine = view.getEngine();


        engine.titleProperty().addListener(new ChangeListener<String>() {
          @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, final String newValue) {
            SwingUtilities.invokeLater(new Runnable() {
              @Override 
                public void run() {
                browser.this.setTitle("WWW browser - "+newValue);
              }
            }
            );
          }
        }
        );

        engine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
          @Override 
            public void handle(final WebEvent<String> event) {
            SwingUtilities.invokeLater(new Runnable() {
              @Override 
                public void run() {
                lblStatus.setText(event.getData());
              }
            }
            );
          }
        }
        );

        engine.locationProperty().addListener(new ChangeListener<String>() {
          @Override
            public void changed(ObservableValue<? extends String> ov, String oldValue, final String newValue) {
            SwingUtilities.invokeLater(new Runnable() {
              @Override 
                public void run() {
                txtURL.setText(newValue);
              }
            }
            );
          }
        }
        );

        engine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
          @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, final Number newValue) {
            SwingUtilities.invokeLater(new Runnable() {
              @Override 
                public void run() {
                if (newValue.intValue() == 100) {
                  progressBar.setVisible(false);
                } else {
                  progressBar.setValue(newValue.intValue()); 
                  progressBar.setVisible(true);
                }
              }
            }
            );
          }
        }
        );

        engine.getLoadWorker()
          .exceptionProperty()
          .addListener(new ChangeListener<Throwable>() {

          public void changed(ObservableValue<? extends Throwable> o, Throwable old, final Throwable value) {
            if (engine.getLoadWorker().getState() == FAILED) {
              SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                  JOptionPane.showMessageDialog(
                    panel, 
                    (value != null) ?
                    engine.getLocation() + "\n" + value.getMessage() :
                    engine.getLocation() + "\nUnexpected error.", 
                    "Loading error...", 
                    JOptionPane.ERROR_MESSAGE);
                }
              }
              );
            }
          }
        }
        );

        jfxPanel.setScene(new Scene(view));
      }
    }
    );
  }
  public static boolean exists(String surl) {

    try {
      URL url = new URL(surl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("HEAD");
      return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
    } 
    catch (IOException e) {
      return false;
    }
  }
  public void loadURL(final String url) {
    Platform.runLater(new Runnable() {
      @Override 
        public void run() {
        String tmp = toURL(url);

        if (tmp == null) {
          tmp = toURL("http://" + url);
        }

        if (exists(tmp)) {
          engine.load(tmp);
        } else {
          engine.load(Ser(txtURL.getText()));
        }
      }
    }
    );
  }
  public String Ser(String txt) {
    txt = txt.replaceAll(" ", "+");
    String m  = "http://www.google.de/search?q="+txt;

    return m;
  }
  private static String toURL(String str) {
    try {
      return new URL(str).toExternalForm();
    } 
    catch (MalformedURLException exception) {
      return null;
    }
  }



  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        browser browser = new browser();
        browser.setVisible(true);

        browser.loadURL("http://google.com");
      }
    }
    );
  }
}
