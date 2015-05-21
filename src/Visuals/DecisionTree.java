package Visuals;


import java.awt.BorderLayout;

import javax.swing.JOptionPane;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;
import Main.Gui;

/**
 * Displays a trained J48 as tree.
 * Expects an ARFF filename as first argument.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DecisionTree {
 
   public void DTree(Classifier cls) throws Exception{

  if(cls == null){
	  JOptionPane.showMessageDialog(Gui.contentPane, "Select decision tree as classifier first!");
	  return;
  }
    final javax.swing.JFrame jf = 
      new javax.swing.JFrame("Decision tree");
    jf.setSize(500,400);
    jf.getContentPane().setLayout(new BorderLayout());
    TreeVisualizer tv = new TreeVisualizer(null, ((J48) cls).graph(), new PlaceNode2());
    jf.getContentPane().add(tv, BorderLayout.CENTER);
    jf.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent e) {
        jf.dispose();
      }
    });

    jf.setVisible(true);
    tv.fitToScreen();
 
    
   }
}