import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.JOptionPane;

class pro extends Frame implements ActionListener
{

Label title,l1,l2;
TextField t1,t2;
Button login,register,forgot;

Connection con;
PreparedStatement pst;
ResultSet rs;

pro()
{

setTitle("JS Medicals Login");
setSize(400,380);
setLayout(null);
setBackground(new Color(135,206,235));

title=new Label("JS Medicals");
title.setBounds(110,80,220,30);
title.setFont(new Font("Arial",Font.BOLD,22));
add(title);

l1=new Label("Customer ID");
l1.setBounds(50,150,120,30);
add(l1);

t1=new TextField();
t1.setBounds(180,150,150,30);
add(t1);

l2=new Label("Password");
l2.setBounds(50,200,120,30);
add(l2);

t2=new TextField();
t2.setEchoChar('*');
t2.setBounds(180,200,150,30);
add(t2);

login=new Button("Login");
login.setBounds(50,260,80,30);
add(login);

register=new Button("Register");
register.setBounds(150,260,80,30);
add(register);

forgot=new Button("Forgot");
forgot.setBounds(250,260,80,30);
add(forgot);

login.addActionListener(this);
register.addActionListener(this);
forgot.addActionListener(this);

connectDB();

setVisible(true);

}

void connectDB()
{

try
{

Class.forName("oracle.jdbc.OracleDriver");

con=DriverManager.getConnection(
"jdbc:oracle:thin:@localhost:1521:XE",
"system",
"2007");

}
catch(Exception e)
{
System.out.println(e);
}

}

public void actionPerformed(ActionEvent e)
{

if(e.getSource()==register)
{

String id=t1.getText();
String pass=t2.getText();

if(pass.length()<4)
{
JOptionPane.showMessageDialog(this,"Password must be at least 4 characters");
return;
}

try
{

pst=con.prepareStatement(
"insert into DETAILS values(?,?,0)");

pst.setString(1,id);
pst.setString(2,pass);

pst.executeUpdate();

JOptionPane.showMessageDialog(this,"Registered Successfully");

}
catch(Exception ex)
{
System.out.println(ex);
}

}

if(e.getSource()==login)
{

try
{

pst=con.prepareStatement(
"select * from DETAILS where customerID=? and password=?");

pst.setString(1,t1.getText());
pst.setString(2,t2.getText());

rs=pst.executeQuery();

if(rs.next())
{

JOptionPane.showMessageDialog(this,"Login Successful");

new MedicineWindow(t1.getText());

dispose();

}

else
JOptionPane.showMessageDialog(this,"Invalid Login");

}
catch(Exception ex)
{
System.out.println(ex);
}

}

if(e.getSource()==forgot)
{

String id=JOptionPane.showInputDialog(this,"Enter Customer ID");

if(id==null) return;

try
{

pst=con.prepareStatement(
"select password from DETAILS where customerID=?");

pst.setString(1,id);

rs=pst.executeQuery();

if(rs.next())
{

String newPass=JOptionPane.showInputDialog(this,"Enter New Password");

pst=con.prepareStatement(
"update DETAILS set password=? where customerID=?");

pst.setString(1,newPass);
pst.setString(2,id);

pst.executeUpdate();

JOptionPane.showMessageDialog(this,"Password Updated Successfully");

}
else
JOptionPane.showMessageDialog(this,"Customer ID not found");

}
catch(Exception ex)
{
System.out.println(ex);
}

}

}

public static void main(String args[])
{
new pro();
}

}



class MedicineWindow extends Frame implements ActionListener
{

Label l1,l2,l3,l4;

TextField priceField,discountField,walletField;

Choice medicineList;

Button show,buy,logout;

Connection con;
PreparedStatement pst;
ResultSet rs;

String user;

MedicineWindow(String user)
{

this.user=user;

setTitle("Apollo Medical Shop");
setSize(450,400);
setLayout(null);
setBackground(new Color(255,182,193));

l1=new Label("Select Medicine");
l1.setBounds(50,80,150,30);
add(l1);

medicineList=new Choice();
medicineList.setBounds(200,80,150,30);
add(medicineList);

l2=new Label("Price");
l2.setBounds(50,130,150,30);
add(l2);

priceField=new TextField();
priceField.setBounds(200,130,150,30);
priceField.setEditable(false);
add(priceField);

l3=new Label("Discount %");
l3.setBounds(50,180,150,30);
add(l3);

discountField=new TextField();
discountField.setBounds(200,180,150,30);
discountField.setEditable(false);
add(discountField);

l4=new Label("Wallet Coins");
l4.setBounds(50,230,150,30);
add(l4);

walletField=new TextField();
walletField.setBounds(200,230,150,30);
walletField.setEditable(false);
add(walletField);

show=new Button("Show");
show.setBounds(50,300,80,30);
add(show);

buy=new Button("Buy");
buy.setBounds(150,300,80,30);
add(buy);

logout=new Button("Logout");
logout.setBounds(250,300,80,30);
add(logout);

show.addActionListener(this);
buy.addActionListener(this);
logout.addActionListener(this);

connectDB();
loadMedicines();
loadWallet();

setVisible(true);

}

void connectDB()
{

try
{

Class.forName("oracle.jdbc.OracleDriver");

con=DriverManager.getConnection(
"jdbc:oracle:thin:@localhost:1521:XE",
"system",
"2007");

}
catch(Exception e)
{
System.out.println(e);
}

}

void loadMedicines()
{

try
{

pst=con.prepareStatement("select mname from medicines");

rs=pst.executeQuery();

while(rs.next())
medicineList.add(rs.getString(1));

}
catch(Exception e)
{
System.out.println(e);
}

}

void loadWallet()
{

try
{

pst=con.prepareStatement(
"select wallet from DETAILS where customerID=?");

pst.setString(1,user);

rs=pst.executeQuery();

if(rs.next())
walletField.setText(rs.getString(1));

}
catch(Exception e)
{
System.out.println(e);
}

}

public void actionPerformed(ActionEvent e)
{

if(e.getSource()==show)
{

try
{

pst=con.prepareStatement(
"select price,discount from medicines where mname=?");

pst.setString(1,medicineList.getSelectedItem());

rs=pst.executeQuery();

if(rs.next())
{

priceField.setText(rs.getString(1));
discountField.setText(rs.getString(2));

}

}
catch(Exception ex)
{
System.out.println(ex);
}

}

if(e.getSource()==buy)
{

try
{

double price=Double.parseDouble(priceField.getText());
double discount=Double.parseDouble(discountField.getText());

double finalPrice=price-(price*discount/100);

new Payment(user,medicineList.getSelectedItem(),price,discount,finalPrice);

}
catch(Exception ex)
{
System.out.println(ex);
}

}

if(e.getSource()==logout)
{

new pro();
dispose();

}

}

}



class Payment extends Frame implements ActionListener,TextListener
{

Label l1,l2,l3;

TextField amountField,walletField,useCoinsField;

Button pay,close;

Connection con;
PreparedStatement pst;
ResultSet rs;

String user,medicine;

double price,discount,amount,originalAmount;

Payment(String user,String medicine,double price,double discount,double amount)
{

this.user=user;
this.medicine=medicine;
this.price=price;
this.discount=discount;
this.amount=amount;
this.originalAmount=amount;

setTitle("Payment Window");
setSize(400,340);
setLayout(null);
setBackground(new Color(144,238,144));

l1=new Label("Final Amount");
l1.setBounds(50,60,120,30);
add(l1);

amountField=new TextField();
amountField.setBounds(170,60,120,30);
amountField.setEditable(false);
amountField.setText(String.format("%.2f",amount));
add(amountField);

l2=new Label("Wallet Coins");
l2.setBounds(50,110,120,30);
add(l2);

walletField=new TextField();
walletField.setBounds(170,110,120,30);
walletField.setEditable(false);
add(walletField);

l3=new Label("Use Coins");
l3.setBounds(50,160,120,30);
add(l3);

useCoinsField=new TextField("0");
useCoinsField.setBounds(170,160,120,30);
useCoinsField.addTextListener(this);
add(useCoinsField);

pay=new Button("Pay");
pay.setBounds(70,230,80,30);
add(pay);

close=new Button("Close");
close.setBounds(200,230,80,30);
add(close);

pay.addActionListener(this);
close.addActionListener(this);

connectDB();
loadWallet();

setVisible(true);

}

void connectDB()
{

try
{

Class.forName("oracle.jdbc.OracleDriver");

con=DriverManager.getConnection(
"jdbc:oracle:thin:@localhost:1521:XE",
"system",
"2007");

}
catch(Exception e)
{
System.out.println(e);
}

}

void loadWallet()
{

try
{

pst=con.prepareStatement(
"select wallet from DETAILS where customerID=?");

pst.setString(1,user);

rs=pst.executeQuery();

if(rs.next())
walletField.setText(rs.getString(1));

}
catch(Exception e)
{
System.out.println(e);
}

}

public void textValueChanged(TextEvent e)
{

try
{

double wallet=Double.parseDouble(walletField.getText());
double coins=Double.parseDouble(useCoinsField.getText());

if(coins>wallet)
{
JOptionPane.showMessageDialog(this,"Not enough wallet coins");
useCoinsField.setText("0");
coins=0;
}

double newAmount=originalAmount-coins;

if(newAmount<0)
newAmount=0;

amountField.setText(String.format("%.2f",newAmount));

}
catch(Exception ex)
{
amountField.setText(String.format("%.2f",originalAmount));
}

}

public void actionPerformed(ActionEvent e)
{

if(e.getSource()==pay)
{

try
{

double wallet=Double.parseDouble(walletField.getText());
double useCoins=Double.parseDouble(useCoinsField.getText());
double finalPay=Double.parseDouble(amountField.getText());

double reward=finalPay*0.10;

double newWallet=wallet-useCoins+reward;

pst=con.prepareStatement(
"update DETAILS set wallet=? where customerID=?");

pst.setDouble(1,newWallet);
pst.setString(2,user);
pst.executeUpdate();

new Receipt(user,medicine,price,discount,useCoins,finalPay,reward,newWallet);

}
catch(Exception ex)
{
System.out.println(ex);
}

}

if(e.getSource()==close)
dispose();

}

}



class Receipt extends Frame
{

TextArea bill;

Receipt(String user,String medicine,double price,double discount,
double usedCoins,double paid,double reward,double wallet)
{

setTitle("Apollo Medical Receipt");

setSize(400,400);
setLayout(new BorderLayout());

bill=new TextArea();
bill.setFont(new Font("Monospaced",Font.BOLD,14));

add(bill);

bill.append("JS MEDICALS...\n");
bill.append("---------------------------------\n");

bill.append("Customer ID : "+user+"\n");
bill.append("Medicine    : "+medicine+"\n");

bill.append("Price       : "+price+"\n");
bill.append("Discount %  : "+discount+"\n");

bill.append("---------------------------------\n");

bill.append("Wallet Used : "+usedCoins+"\n");
bill.append("Amount Paid : "+paid+"\n");
bill.append("Reward Coin : "+reward+"\n");

bill.append("---------------------------------\n");

bill.append("Wallet Balance : "+wallet+"\n");

bill.append("---------------------------------\n");
bill.append("THANK YOU VISIT AGAIN\n");

setVisible(true);

}

}