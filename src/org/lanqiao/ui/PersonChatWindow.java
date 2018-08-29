package org.lanqiao.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PersonChatWindow {
	JFrame fs;

	JTextArea tas;

	String name;

	public PersonChatWindow(String username) {
		this.name = username;
		fs = new JFrame();
		fs.setSize(400, 200);
		/*fs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
		fs.setLocation(300, 200);
		fs.setTitle("与" + name + "私聊");
		fs.setLocation(300, 200);
		tas = new JTextArea();
		JScrollPane sps = new JScrollPane(tas);
		tas.setEditable(false);
		final JTextField tfs = new JTextField();
		JPanel ps = new JPanel(new BorderLayout());
		// JComboBox cbs = new JComboBox();
		// cbs.addItem(name);
		// ps.add(cbs, BorderLayout.WEST);
		ps.add(tfs);
		fs.getContentPane().add(ps, BorderLayout.SOUTH);
		fs.getContentPane().add(sps);
		fs.setVisible(true);//将窗口显示出来
	}
}
