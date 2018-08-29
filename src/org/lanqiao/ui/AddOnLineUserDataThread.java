package org.lanqiao.ui;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.lanqiao.message.GetMessageThread;
import org.lanqiao.util.DBConnection;

public class AddOnLineUserDataThread extends Thread {
	String username;
	ChatWindow cw;

	int tcpPort;
	public AddOnLineUserDataThread(String username, ChatWindow cw,int tcpPort) {
		this.username = username;
		this.cw = cw;
		this.tcpPort=tcpPort;
	}

	public void run() {
		// ����һ���µ��̣߳���������UDP���񣨴�8888�˿ڣ��п��ܲ���8888����ʱ׼��������Ϣ��
		GetMessageThread gmt = new GetMessageThread(cw);
		gmt.start();

		// ��ʼ�������
		Connection conn = null;
		try {
			conn = DBConnection.getConn();
			conn.setAutoCommit(false);
			String sql = "INSERT INTO tb_online_user(username,ip,udpport,tcpport) VALUES(?,?,?,?)";
			// ������������Statement����
			PreparedStatement pstmt = conn.prepareStatement(sql);
			String ip = InetAddress.getLocalHost().getHostAddress();
			// ��ñ���IP
			pstmt.setString(1, username);
			pstmt.setString(2, ip);// IP
			pstmt.setInt(3, GetMessageThread.port);// Port
			pstmt.setInt(4,tcpPort);

			// ���Ĳ���ִ��SQL���
			pstmt.executeUpdate();// ������ݣ�ִ�и���
			conn.commit();

		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} finally {
			try {
				// ���������ر����ݿ�����
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// ��ʼ��ʾXX����

		try {
			conn = DBConnection.getConn();
			String sql = "SELECT * FROM tb_online_user";

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);// ִ�в�ѯ

			DatagramSocket ds = new DatagramSocket();

			while (rs.next()) {
				String ip = rs.getString("IP");
				int port = rs.getInt("UDPPORT");
				String name = rs.getString("USERNAME");

				if (!name.equals(username)) {
					// ���Լ���XX�������ң����Լ������촰�����XX���������ң�
					cw.ta.append(name + "����������\n");
					cw.ta.setCaretPosition(cw.ta.getText().length());
					cw.cb.addItem(name);
					String message = username + "������\n";
					// ����Ϣ
					DatagramPacket dp = new DatagramPacket(message.getBytes(),
							0, message.getBytes().length);
					// 192.168.2.220
					byte ipByte[] = new byte[4];
					String str[] = ip.split("\\.");

					ipByte[0] = (byte) Integer.parseInt(str[0]);
					ipByte[1] = (byte) Integer.parseInt(str[1]);
					ipByte[2] = (byte) Integer.parseInt(str[2]);
					ipByte[3] = (byte) Integer.parseInt(str[3]);

					dp.setAddress(InetAddress.getByAddress(ipByte));// ����IP
					dp.setPort(port);// ���ö˿�
					ds.send(dp);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}