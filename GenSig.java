package AuctioningSystem;
import java.io.*;
import java.security.*;

class GenSig {

    public static void main(String[] args) {

        /* Generate a DSA signature */

    try {
        // the rest of the code goes here
        //Generate Keys
            FileOutputStream fop;
            ObjectOutputStream oos, privatekeyoos;
            File file;
            File keyfile = new File("publickeys.key");
            User currentuser;
            PrivateKey priv;
            PublicKey pub;
            PublicKey serverpub;
            String[] names = {"Jack", "George", "Amy", "Nicole", "James"};
            String[] emails = {"jack@gmail.com", "george@hotmail.com", "amy@amy.com", "nicole@gmail.com", "james@james.com"};
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);
            KeyPair pair = keyGen.generateKeyPair();
            serverpub = pair.getPublic();
            priv = pair.getPrivate();
            file = new File("serverprivate.key");
            fop = new FileOutputStream(file);
            oos = new ObjectOutputStream(fop);
            oos.writeObject(priv);
            fop.close();
            oos.close();



            for(int i = 1; i <= 5; i++){
                pair = keyGen.generateKeyPair();
                priv = pair.getPrivate();
                pub = pair.getPublic();
                currentuser = new User(priv,serverpub, names[i-1], emails[i-1], i);
                //Add client public key to publickey file
                file = new File("public" + i + ".key");
                fop = new FileOutputStream(file);
                oos = new ObjectOutputStream(fop);
                oos.writeObject(pub);
                oos.close();
                //Add private keys into separate files id.key
                file = new File("user" + i + ".ser");
                fop = new FileOutputStream(file);
                oos = new ObjectOutputStream(fop);
                oos.writeObject(currentuser);
                fop.close();
                oos.close();
            }       


        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }

   
}
