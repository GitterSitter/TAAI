package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.clusterers.EM;
import weka.clusterers.SimpleKMeans;
import weka.clusterers.XMeans;
import Util.Filtering;
import Util.quickAndAlgo;
import Visuals.ClusterVisual;

public class Gui extends JFrame {
	private JFileChooser chooser;
	public static JPanel contentPane;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenu mnFile;
	private JMenuItem mntmSaveModel;
	private JMenuItem mntmLoadModel;
	private JMenu mnSupervised;
	private JMenuItem mntmNaivebayes;
	private JMenuItem mntmSvm;
	private JMenuItem mntmNeuralNetwork;
	private JMenu mnUnsupervised;
	private JMenuItem mntmKmeans;
	private JMenuItem mntmXmeans;
	private JMenuItem mntmEm;
	private JMenuItem mntmSelectFolder;
	private JMenu mnVisualize;
	private JMenuItem mntmClusterPlot;
	private Filtering filter;
	public static boolean isValidating;
	/**
	 * Launch the application.
	 */
	boolean buttonChoice = false;
	String dir = "";

	public static void main(String[] args) {

		try {
			Gui frame = new Gui();
			frame.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the frame.
	 * 
	 * @throws Exception
	 */
	public Gui() throws Exception {

		super("Test");
		setTitle("TAAI");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 755, 693);

		filter = new Filtering();
		dir = "testtest";

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmSelectFolder = new JMenuItem("Select Folder");
		mntmSelectFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				chooser = new JFileChooser();
				String choosertitle = "";

				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle(choosertitle);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					System.out.println("Dir: " + chooser.getCurrentDirectory());
					dir = chooser.getSelectedFile().getPath();
					System.out.println("getSelectedFile() : "
							+ chooser.getSelectedFile());
				} else {

					System.out.println("No Selection ");
				}
			}

		});
		mnFile.add(mntmSelectFolder);

		mntmSaveModel = new JMenuItem("Save Model");
		mntmSaveModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ObjectOutputStream oos;
				try {

					System.out.println(filter.getClassy() + " Class "
							+ filter.getClassy().getClass());
					oos = new ObjectOutputStream(new FileOutputStream(filter
							.getClassy().getClass() + ".model"));
					oos.writeObject(filter.getClassy());
					oos.flush();
					oos.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		mnFile.add(mntmSaveModel);

		mntmLoadModel = new JMenuItem("Load Model");
		mntmLoadModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File fil = null;
				chooser = new JFileChooser();
				String choosertitle = "";

				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle(choosertitle);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setAcceptAllFileFilterUsed(true);
				if (chooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					System.out.println("Dir: " + chooser.getCurrentDirectory());

					fil = chooser.getSelectedFile();
				} else {
					System.out.println("No Selection ");
				}

				try {
					ObjectInputStream ois = new ObjectInputStream(
							new FileInputStream(fil.getAbsolutePath()));
					Classifier cls = (Classifier) ois.readObject();
					filter.setClassy(cls);
					ois.close();

					System.out.println(cls.getClass());
				} catch (FileNotFoundException e1) {

					e1.printStackTrace();
				} catch (IOException e1) {

					e1.printStackTrace();
				} catch (ClassNotFoundException ee) {

					ee.printStackTrace();
				}

			}
		});
		mnFile.add(mntmLoadModel);

		mnNewMenu = new JMenu("Methods");
		menuBar.add(mnNewMenu);

		mnSupervised = new JMenu("Supervised");
		mnNewMenu.add(mnSupervised);

		mntmNeuralNetwork = new JMenuItem("Neural Network");
		mntmNeuralNetwork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					if (dir == "") {

						javax.swing.JOptionPane.showMessageDialog(contentPane,
								"Please select folder with test data!");
					}

					MultilayerPerceptron neural = new MultilayerPerceptron();
					setText(filter.classify(neural, dir, buttonChoice));
				} catch (Exception e1) {
					// e1.printStackTrace();
				}
			}
		});
		mnSupervised.add(mntmNeuralNetwork);

		mntmSvm = new JMenuItem("SVM");
		mntmSvm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					SMO smo = new SMO();
					if (dir == "") {
						javax.swing.JOptionPane.showMessageDialog(contentPane,
								"Please select folder with test data!");
					}

					setText(filter.classify(smo, dir, buttonChoice));
				} catch (Exception e1) {

					// e1.printStackTrace();
				}
			}
		});
		mnSupervised.add(mntmSvm);

		mntmNaivebayes = new JMenuItem("NaiveBayes");
		mntmNaivebayes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					NaiveBayes naive = new NaiveBayes();
					if (dir == "") {
						javax.swing.JOptionPane.showMessageDialog(contentPane,
								"Please select folder with test data!");
					}

					setText(filter.classify(naive, dir, buttonChoice));
				} catch (Exception e1) {

				 e1.printStackTrace();
				}
			}
		});
		mnSupervised.add(mntmNaivebayes);

		mnUnsupervised = new JMenu("Unsupervised");
		mnNewMenu.add(mnUnsupervised);

		mntmKmeans = new JMenuItem("KMeans");
		mntmKmeans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				SimpleKMeans km = new SimpleKMeans();
				try {
					setText(filter.cluster(km));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		mnUnsupervised.add(mntmKmeans);

		mntmXmeans = new JMenuItem("XMeans");
		mntmXmeans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				XMeans xm = new XMeans();
				try {
					setText(filter.cluster(xm));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnUnsupervised.add(mntmXmeans);

		mntmEm = new JMenuItem("EM");
		mntmEm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				EM em = new EM();

				try {
					setText(filter.cluster(em));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnUnsupervised.add(mntmEm);

		mnVisualize = new JMenu("Visualize");
		menuBar.add(mnVisualize);

		mntmClusterPlot = new JMenuItem("Cluster Plot");
		mntmClusterPlot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClusterVisual visual = new ClusterVisual();
				try {

					visual.visuals(filter.getClus(), filter.getInst(),
							filter.getEvaluation());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnVisualize.add(mntmClusterPlot);

		JMenu mnFilter = new JMenu("Filter");
		menuBar.add(mnFilter);

		JMenuItem mntmAndorithm = new JMenuItem("AndO-rithm");
		mntmAndorithm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					setText(quickAndAlgo.readAndDecide());

				} catch (FileNotFoundException e1) {

					e1.printStackTrace();
				}
			}
		});
		mnFilter.add(mntmAndorithm);

		JMenuItem mntmInfogain = new JMenuItem("Infogain");
		mntmInfogain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					setText(filter.findBestAtt());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		mnFilter.add(mntmInfogain);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.text);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 266, 719, 367);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		ButtonGroup buttonGroup1 = new ButtonGroup();
		ButtonGroup buttonGroup2 = new ButtonGroup();

		JRadioButton rdbtnNewRadioButton = new JRadioButton("10 Attributes");
		rdbtnNewRadioButton.setSelected(true);
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonChoice = false;
			}
		});
		rdbtnNewRadioButton.setBackground(Color.WHITE);
		rdbtnNewRadioButton.setBounds(135, 79, 109, 23);
		buttonGroup1.add(rdbtnNewRadioButton);
		contentPane.add(rdbtnNewRadioButton);

		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("1000 Attributes");
		rdbtnNewRadioButton_1.setSelected(true);
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonChoice = true;
			}
		});
		rdbtnNewRadioButton_1.setBackground(Color.WHITE);
		rdbtnNewRadioButton_1.setBounds(135, 105, 122, 23);
		buttonGroup1.add(rdbtnNewRadioButton_1);
		contentPane.add(rdbtnNewRadioButton_1);

		JLabel lblAttributeSelection = new JLabel("Attribute selection");
		lblAttributeSelection.setFont(new Font("Sylfaen", Font.PLAIN, 20));
		lblAttributeSelection.setBounds(125, 25, 169, 47);
		contentPane.add(lblAttributeSelection);
		
		JLabel lblEvaluateTrainingModel = new JLabel("Evaluate classifier model");
		lblEvaluateTrainingModel.setFont(new Font("Sylfaen", Font.PLAIN, 20));
		lblEvaluateTrainingModel.setBounds(344, 25, 260, 47);
		contentPane.add(lblEvaluateTrainingModel);
		
		final JRadioButton rdbtnCrossValidateModel = new JRadioButton("Cross validate model (10 folds)");
		rdbtnCrossValidateModel.setSelected(true);
		rdbtnCrossValidateModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnCrossValidateModel.isSelected()){
					rdbtnCrossValidateModel.setSelected(true);
					isValidating = true;
				}else 
					isValidating = false;
				
				System.out.println(isValidating);
			}
		});
		rdbtnCrossValidateModel.setBackground(Color.WHITE);
		rdbtnCrossValidateModel.setBounds(364, 79, 208, 23);
		contentPane.add(rdbtnCrossValidateModel);
		
		final JRadioButton rdbtnUseOf = new JRadioButton("Use 1/3 of traning data as test");
		rdbtnUseOf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnUseOf.isSelected()){
					rdbtnUseOf.setSelected(true);
					isValidating = false;
				}else
					isValidating = true;
				
				System.out.println(isValidating);
				
			}
		});
		rdbtnUseOf.setBackground(Color.WHITE);
		rdbtnUseOf.setBounds(364, 105, 240, 23);
		contentPane.add(rdbtnUseOf);

		

		buttonGroup2.add(rdbtnUseOf);
		buttonGroup2.add(rdbtnCrossValidateModel);
		
	}

	public void setText(String text) {
		textArea.setText(text);
	}
}