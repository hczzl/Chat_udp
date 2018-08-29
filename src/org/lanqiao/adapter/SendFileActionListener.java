package org.lanqiao.adapter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import org.lanqiao.util.DBConnection;

public class SendFileActionListener implements ActionListener {

	JComboBox cb;

	public SendFileActionListener(JComboBox cb) {
		this.cb = cb;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * JFileChooser�ࣺΪ�û�ѡ���ļ��ṩ��һ�ּ򵥵Ļ���
		 */
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(null);// ����һ�� "Open File" �ļ�ѡ�����Ի���,����һ���������ݣ�����Ϊnull��ʲô��˼
		if (returnVal == JFileChooser.APPROVE_OPTION) {// �������ȷ����ѡ��ȷ�ϣ�yes��ok���󷵻ظ�ֵ��
			String fileName = chooser.getSelectedFile().getAbsolutePath();// ������ ����ѡ�е��ļ���Ȼ�� ���ش˳���·�����ľ���·�����ַ�����
			//�ļ���·��
			Socket socket = null;
			FileInputStream fis = null;
			/**FileInputStream
			 *  ���ļ�ϵͳ�е�ĳ���ļ��л�������ֽ�
			 *  ���ڶ�ȡ����ͼ������֮���ԭʼ�ֽ�����Ҫ��ȡ�ַ������뿼��ʹ�� FileReader��
			 */
			try {

				Connection conn = null;
				conn = DBConnection.getConn();
				String sql = "SELECT ip,tcpport FROM tb_online_user WHERE username=?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, ((String) cb.getSelectedItem()).trim());// �Ȼ�ȡJComboBox��ѡ��Ҫ��˭�����ļ���Ȼ�����ǰ��Ŀո���ǿתΪString
				ResultSet rs = pstmt.executeQuery();// ִ�в�ѯ
				rs.next();
				String ip = rs.getString("IP");
				int tcpPort = rs.getInt("TCPPORT");

				// �����ļ�---����socket
				socket = new Socket(ip, tcpPort);// ����Socket����
				//����һ�����׽��ֲ��������ӵ�ָ�� IP ��ַ��ָ���˿ںš�
				OutputStream os = socket.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);
				/**
				 * �������������Ӧ�ó������ʵ���ʽ������ Java ��������д��������С�
				 */
				dos.writeUTF(chooser.getSelectedFile().getName());// �ȷ����ļ������Է�����
				// ��������޹ط�ʽʹ�� UTF-8 �޸İ���뽫һ���ַ���д������������
				dos.flush();
				fis = new FileInputStream(fileName);
				int a = 0;
				byte by[] = new byte[1024 * 8];
				while ((a = fis.read(by)) != -1) {//�Ӵ��������н���� b.length ���ֽڵ����ݶ���һ�� byte �����С�
					dos.write(by, 0, a);//  ��ָ�� byte �����д�ƫ���� off ��ʼ�� len ���ֽ�д������������
					dos.flush();
				}
				/*
				 * OutputStreamWriter osw=new OutputStreamWriter(os); PrintWriter out=new
				 * PrintWriter(osw);
				 * out.println(chooser.getSelectedFile().getName());//�ȷ����ļ������Է�����----
				 * �����ｫ�ļ��ĵ����ַ��ͳ�ȥ��getName out.flush();//ˢ�»�����
				 */ /*
					 * FileInputStream fis=new FileInputStream(fileName); int a=0; byte by[]=new
					 * byte[1024*8]; while((a=fis.read(by))!=-1)//�����ļ����� {
					 * 
					 * }
					 */

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				try {
					fis.close();//�ر�FileInpuStream
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}
	}
}