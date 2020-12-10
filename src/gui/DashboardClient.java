package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import core.Building;
import core.Client;
import core.Material;
import core.Order;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

public class DashboardClient extends JFrame {
	private String Username; 
	private int ClientId; 
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel tm; 
	
	private void loadTable()
	{
		ArrayList<Building> buildings = new Building().getBuildingsOwned(this.ClientId);
		tm.setRowCount(0);
		
		for(Building building : buildings)
		{
			Object[] objs = {building.BuildingId, building.BuildingName, building.BuildingType, building.ConstructionYear, building.SiteName, building.SiteLocation};
			tm.addRow(objs);
		}
	}
	
	public DashboardClient(int ClientId, String Username)  {
		this.Username = Username;
		this.ClientId = ClientId; 
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(400, 100, 800, 600);
		setResizable(false);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		menuBar.add(toolBar);
		
		JButton btnLogout = new JButton("Logout");
		toolBar.add(btnLogout);
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Login();
				dispose(); 
			}
		});
		
		JLabel lblWelcome = new JLabel("Welcome, " + this.Username);
		menuBar.add(lblWelcome);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(102, 153, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		setTitle("Dashboard - Client");
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		String cols[] = {"Building ID","Building Name", "Building Type" , "Year of Construction" , "Site Name", "Location" };
		tm = new DefaultTableModel(cols, 0);
		JTable table = new JTable(tm) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {                
                return false;               
			}
		};
		
		table.addMouseListener(new MouseAdapter() {
	         public void mouseClicked(MouseEvent me) {
		            if (me.getClickCount() == 2) {    
		               JTable target = (JTable)me.getSource();
		               int row = target.getSelectedRow();
		               int BuildingId = Integer.parseInt(table.getValueAt(row, 0).toString());
		               
		               Building b = new Building(BuildingId);
		               
		               
		               JTextArea textArea = new JTextArea(b.feedback);
		               textArea.setColumns(30);
		               textArea.setLineWrap(true);
		               textArea.setWrapStyleWord(true);
		               JOptionPane.showMessageDialog(null, new JScrollPane( textArea), "Feedback Form", JOptionPane.INFORMATION_MESSAGE); 
		               String feedback = textArea.getText(); 
		               if (feedback != null && feedback.length() > 0 && feedback != b.feedback)
		               {
		            	   b.feedback = feedback;
		            	   if (b.updateDetails()) {
		            		   JOptionPane.showMessageDialog(null, "Thank you for your valuable feedback.");
		            	   }
		               }
		            }
	         }
		}); 
		
		tabbedPane.addTab("My Buildings", (new JScrollPane(table)));
		loadTable(); 
		getContentPane().add(tabbedPane);
		setVisible(true); 
	}
}
