package Visuals;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Clusterer;
import weka.core.Instances;
import weka.gui.explorer.ClustererPanel;
import weka.gui.visualize.Plot2D;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.VisualizePanel;
import Main.Gui;

public class ClusterVisual {

	public void visuals(Clusterer clusterer, Instances train,
			ClusterEvaluation eval) {
		
		if (train == null) {
			JOptionPane.showMessageDialog(Gui.contentPane,
					"You have to use cluster!");
		}

		try {
			PlotData2D predData = ClustererPanel.setUpVisualizableInstances(
					train, eval);
			int[] m_shapeType = new int[train.numInstances()];
			for (int i = 0; i < train.numInstances(); i++) {
				m_shapeType[i] = Plot2D.X_SHAPE;

			}

			predData.setShapeType(m_shapeType);
			String name = (new SimpleDateFormat("HH:mm:ss - "))
					.format(new Date());
			String cname = clusterer.getClass().getName();
			if (cname.startsWith("weka.clusterers."))
				name += cname.substring("weka.clusterers.".length());
			else
				name += cname;

			VisualizePanel vp = new VisualizePanel();

			vp.setName(name + " (" + train.relationName() + ")");
			predData.setPlotName(name + " (" + train.relationName() + ")");
			vp.addPlot(predData);

			String plotName = vp.getName();
			final javax.swing.JFrame jf = new javax.swing.JFrame(
					"Weka Clusterer Visualize: " + plotName);
			jf.setSize(500, 400);
			jf.getContentPane().setLayout(new BorderLayout());
			jf.getContentPane().add(vp, BorderLayout.CENTER);
			jf.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					jf.dispose();
				}
			});
			jf.setVisible(true);

		} catch (Exception e) {

		}
	}

}
