import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.swing.*;

public class MyJframes extends JFrame {
	private JPanel panel1 = new JPanel();
	private JButton buttona = new JButton("��ʼ");
	private JLabel label1 = new JLabel("����");
	private JTextField textarea1 = new JTextField(10);
	private JLabel buttonc = new JLabel("ʱ��");
	private JProgressBar jindu = new JProgressBar();
	private Timer timer;
	private JButton buttonb = new JButton("�˳�");
	private JPanel panel2 = new JPanel();
	private JButton button[][] = new JButton[8][8];
	private int animal[][] = new int[8][8];
	private ImageIcon Iocn[] = new ImageIcon[7];
	// ����Ƿ����������ϵ�����
	private final int EMPTY = 0;// ��Ϊ0����Ϊ1
	// �����
	private Random rand = new Random();// new һ��������
	// ����Ƿ����������ϵ�����
	private boolean isThreeLinked;
	// ��ǵ�������
	private boolean isDoubleClicked;	
	private int x1;// ��¼��һ�α������ť��X����	
	private int y1;// ��¼��һ�α������ť��Y����
	private int grade = 0; //�÷�
	private Sound sound=new Sound();
	
    
    
	MyJframes() {
		
		// ����ͼƬ
		Iocn[0] = new ImageIcon("image//cat.png");
		Iocn[1] = new ImageIcon("image//cattle.png");
		Iocn[2] = new ImageIcon("image//chicken.png");
		Iocn[3] = new ImageIcon("image//fox.png");
		Iocn[4] = new ImageIcon("image//monkey.png");
		Iocn[5] = new ImageIcon("image//panda.png");
		Iocn[6] = new ImageIcon("image//frog.png");
		panel1.setLayout(new FlowLayout());
		panel1.add(buttona);
		panel1.add(label1);
		panel1.add(textarea1);
		textarea1.setEditable(false);
		textarea1.setText(Integer.toString(grade));//
		panel1.add(buttonc);
		jindu.setMaximum(100);
		jindu.setBackground(new Color(255, 255, 255)); // ����
		panel1.add(jindu);
		panel1.add(buttonb);
		
		
        
		this.setLayout(new BorderLayout());
		this.add(panel1, BorderLayout.NORTH);		
		panel2.setLayout(new GridLayout(8, 8, 1, 1));		
		MyListener mylisten = new MyListener();
		int m;
		// ��ʼ����������
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				m = (int) (Math.random() * 7);
				button[i][j] = new JButton(Iocn[m]);
				animal[i][j] = m;
				button[i][j].setSize(50, 50);
				//button[i][j].setFont(new Font(null, Font.BOLD, 25));
				button[i][j].addActionListener(mylisten);				
				button[i][j].setEnabled(false); //ͼ�ΰ�ť��Ч
				panel2.add(button[i][j]);
			}
		this.add(panel2, BorderLayout.CENTER);		
		buttona.addActionListener(mylisten);
		buttonb.addActionListener(mylisten);
	}

	// ��ʼ����������
	private void initAnimalMatrix() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				// ���ѡȡ����
				animal[i][j] = rand.nextInt(7);
			}
		}
	}
	private void removeLinked(int x, int y) {
		if(animal[x][y] == EMPTY)return;
		int n=0;
		int tmp;
		int linked = 1;
		if (x + 1 < 8) {
			tmp = x + 1;
			while (tmp < 8 && animal[x][y] == animal[tmp][y]) {
				linked++;
				tmp++;
			}
		}
		if (x - 1 >= 0) {
			tmp = x - 1;
			while (tmp >= 0 && animal[x][y] == animal[tmp][y]) {
				linked++;
				tmp--;
			}
		}		
		if (linked >= 3) {
			n=n+linked; 
			tmp = x + 1;
			while (tmp < 8 && animal[tmp][y] == animal[x][y]) {				
				animal[tmp][y] = EMPTY;
				tmp++;
			}
			tmp = x - 1;
			while (tmp >= 0 && animal[tmp][y] == animal[x][y]) {
				animal[tmp][y] = EMPTY;
				tmp--;
			}
			// ��ǰ���������ĵ�
			animal[x][y] = EMPTY;
		}
		tmp = 0;
		linked = 1;
		if (y + 1 < 8) {
			tmp = y + 1;
			while (tmp < 8 && animal[x][y] == animal[x][tmp]) {
				linked++;
				tmp++;
			}
		}
		if (y - 1 >= 0) {
			tmp = y - 1;
			while (tmp >= 0 && animal[x][y] == animal[x][tmp]) {
				linked++;
				tmp--;
			}
		}
		if (linked >= 3) {
			n=n+linked; 
			tmp = y + 1;
			while (tmp < 8 && animal[x][y] == animal[x][tmp]) {
				animal[x][tmp] = EMPTY;
				tmp++;
			}
			tmp = y - 1;
			while (tmp >= 0 && animal[x][y] == animal[x][tmp]) {
				animal[x][tmp] = EMPTY;
				tmp--;
			}
			// ��ǰ���������ĵ�
			animal[x][y] = EMPTY;
		}		
		grade+=n*10;
		textarea1.setText(Integer.toString(grade));
		System.out.println(grade + " ======"+x+" "+y);
	}
	private boolean globalSearch(int flag) {
		if (1 == flag) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (isThreeLinked(i, j)) {						
						return true;
					}
				}
			}
		} else if (2 == flag) {
			
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					// ɾ�����������ĵ�
					removeLinked(i, j);

				}
			}
		}
		return false;
	}

	// �Ƿ��������������ӵĵ�
	private boolean isThreeLinked(int x, int y) {
		int tmp;
		int linked = 1;
		if (x + 1 < 8) {
			tmp = x + 1;
			while (tmp < 8 && animal[x][y] == animal[tmp][y]) {
				linked++;
				tmp++;
			}
		}
		if (x - 1 >= 0) {
			tmp = x - 1;
			while (tmp >= 0 && animal[x][y] == animal[tmp][y]) {
				linked++;
				tmp--;
			}
		}
		if (linked >= 3) {

			return true;
		}
		linked = 1;
		if (y + 1 < 8) {
			tmp = y + 1;
			while (tmp < 8 && animal[x][y] == animal[x][tmp]) {
				linked++;
				tmp++;
			}
		}
		if (y - 1 >= 0) {
			tmp = y - 1;
			while (tmp >= 0 && animal[x][y] == animal[x][tmp]) {
				linked++;
				tmp--;
			}
		}
		if (linked >= 3) {

			return true;
		}
		return false;
	}

	// ���¶������
	private void updateAnimal() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (animal[i][j] == EMPTY) {
					// ��������Ϊ��ֵ�ĵ��ٽ��������ֵ
					animal[i][j] = rand.nextInt(7);
				}
			}
		}
	}

	// �����½�
	private void downAnimal() {
		int tmp;
		for (int j = 8 - 1; j >= 0; j--) {
			for (int i = 0; i < 8; i++) {
				if (animal[j][i] == EMPTY) {
					for (int k = j - 1; k >= 0; k--) {
						if (animal[k][i] != EMPTY) {
							tmp = animal[k][i];
							animal[k][i] = animal[j][i];
							animal[j][i] = tmp;
							break;
						}
					}
				}
			}
		}
	}

	private void print() { //������ʾ��ťͼ��
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				button[i][j].setIcon(Iocn[animal[i][j]]);
			}
		}
	}

	private void swapAnimal(int x, int y) {		
		if ((x >= 0 && x <= 8) && (y >= 0 && y <= 8)) {
			// ������Ķ��������
    		int x2;
    		int y2;
			if (!isDoubleClicked) { //��һ�ε���
				isDoubleClicked = true;
				x1 = x;
				y1 = y;
				System.out.println("�������ĵ��X���� = " + x1);
				System.out.println("�������ĵ��Y���� = " + y1);
			} else{               //��2�ε���
	    		x2 = x;
	    		y2 = y;
    			isDoubleClicked = false;
    			//������������ֵ����1ʱ��Ϊ���ڵ�����
    			if (1 == Math.abs(x2 - x1) && y2 == y1
    					|| 1 == Math.abs(y2 - y1)&& x2 == x1) {
    				//-------------�������������ڵ������ֵ--------------
    				int tmp;
    				tmp = animal[y2][x2];
    				animal[y2][x2] = animal[y1][x1];
    				animal[y1][x1] = tmp;
    				//-----------------------------------------------------

    				if (isThreeLinked(y2, x2)||isThreeLinked(y1, x1)) {
    					System.out.println("������");
    					if(isThreeLinked(y2, x2))
    						removeLinked(y2, x2);
    					if(isThreeLinked(y1, x1))
    						removeLinked(y1, x1);
    					downAnimal();//����ȥ���Ϸ��Ķ����½�
    					//�����Ϸ�������������µĶ�����¶������
    					updateAnimal();
    					print();
    					
    					//ȫ��ɨ���ж��Ƿ����µ������������ӵ㣬����ɾ��
    					while (globalSearch(1)) {
    						//ȫ��ɨ�������������������ĵ�
    						globalSearch(2);
    						//�����ٴ�����
    						downAnimal();
    						//�ٴθ��¶������
    						updateAnimal();
    						print();
    						
    					}  
    				}
    				else {//û������������ͬ�ĵ㣬��������  
    					System.out.println("��������");
    					tmp = animal[y1][x1];
    					animal[y1][x1] = animal[y2][x2];
    					animal[y2][x2] = tmp;
    					print();     	    			
    				}
    			}
    		}
    	}
    }
	public void init() {
		do {
			System.out.println("���³�ʼ��");
			initAnimalMatrix();
		} while (globalSearch(1));
		print();
		pack();
		setResizable(false);
		setVisible(true);
	}


	public static void main(String[] args) {
		MyJframes frame = new MyJframes();
		frame.setTitle("�Զ�����Ϸ2012-6-28");
		frame.setSize(500, 500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.init();
	}
	// ������
	class MyListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttona) {
				buttona.setEnabled(false);
				jindu.setStringPainted(true);
				jindu.setMaximum(100);
				jindu.setMinimum(0);
				timer = new Timer(800, new TimeListener());
				timer.start();
				grade = 0;
				textarea1.setText(Integer.toString(grade));
				for (int i = 0; i < 8; i++)
					for (int j = 0; j < 8; j++) {
						button[i][j].setEnabled(true); //ͼ�ΰ�ť��Ч
					}
			}
			if (e.getSource() == buttonb) {
				System.out.println("end");
				System.exit(1);
			}
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (e.getSource() == button[i][j]) {
						//System.out.println("��" + i + " " + j + "��");
						swapAnimal(j, i);
						
					}
				}
			}
		}
		
		
	}

	class TimeListener implements ActionListener {
		int times = 0;
		
		public void actionPerformed(ActionEvent e) {
			jindu.setValue(times++);
			if(times==1){
				if(sound.isplay()){
					sound.mystop();
				}
				
				sound.loadSound();
			}
			if(times==90){
				if(sound.isplay()){
					sound.mystop();
				}
				sound.setMusic("nor.mid");
				sound.loadSound();
				
			}
			if(times==100){
				if(sound.isplay()){
					sound.mystop();
				}
			}
			if(times>90){
				jindu.setForeground(new Color(255, 0, 0)); // ���Ⱥ�ɫ
				
			}else{
				jindu.setForeground(new Color(0, 255, 10)); // ������ɫ
			}
			if (times > 100) {
				timer.stop();
				//��ʱ������
				for (int i = 0; i < 8; i++)
					for (int j = 0; j < 8; j++) {
						button[i][j].setEnabled(false); //ͼ�ΰ�ť��Ч
					}
				buttona.setEnabled(true);
			}
		}

	}
	class Sound
	{
		String path=new String("musics\\");
		String  file=new String("popo.mid");
		Sequence seq;
	    Sequencer midi;
		boolean sign;
		void loadSound()
		{
			try {
	            seq=MidiSystem.getSequence(new File(path+file));
	            midi=MidiSystem.getSequencer();
	            midi.open();
	            midi.setSequence(seq);
				midi.start();
				midi.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
	        }
	        catch (Exception ex) {ex.printStackTrace();}
			sign=true;
		}
		void mystop(){midi.stop();midi.close();sign=false;}
		boolean isplay(){return sign;}
		void setMusic(String e){file=e;}
	}

}
