package org.lanqiao.adapter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.lanqiao.file.ReceiveFileThread;
import org.lanqiao.ui.AddOnLineUserDataThread;
import org.lanqiao.ui.ChatWindow;
import org.lanqiao.util.DBConnection;

public class MyActionListener implements ActionListener {

	JTextField accountJTF;
	JTextField passwordJTF;
	JFrame frame;

	public MyActionListener(JTextField accountJTF, JTextField passwordJTF,
			JFrame frame) {
		this.accountJTF = accountJTF;
		this.passwordJTF = passwordJTF;
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		/*
		 * 1��Ҫ���û����������õ� 2���������ݿ� 3����ʼ��֤�û���������
		 */
		String username = accountJTF.getText();
		String password = passwordJTF.getText();
		Connection conn = null;
		try {
			conn = DBConnection.getConn();
			String sql = "SELECT * FROM tb_user WHERE username=? AND password=?";
			// Statement�����
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();// execute query
			if (rs.next())// ָ����SQL����һ��רҵ���ڣ��α�
			{
				System.out.println("��¼�ɹ�");
				// ��ʼ��ʾ���촰��
				ChatWindow ct = new ChatWindow(username);
				// ����������ݵ��߳�
				
				//������һ���̣߳�ר�����ڽ����ļ���
				ReceiveFileThread rft=new ReceiveFileThread();
				Thread t=new Thread(rft);
				t.start();
				//��TCP�˿���ӵ����ݿ�
				AddOnLineUserDataThread adt = new AddOnLineUserDataThread(
						username, ct,rft.port);
				adt.start();
				frame.setVisible(false);
			} else {
				System.out.println("�û������������");
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
}