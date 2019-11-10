package sesion;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rutas.santaelena.app.rutas.R;

import java.security.MessageDigest;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import denuncias.AbstractAsyncActivity;
import denuncias.Denuncias;
import entities.SegUsuario;
import models.HttpPostUser;

public class RegistraUsuario extends AppCompatActivity{

    EditText editTextMail ;
    EditText editTextNick ;
    EditText editTextMovil ;
    EditText editTextPass;
    EditText editTextConfPass ;
    private static final String AES = "AES";
    static byte[] passwwordEncritado;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registro_usuario);

         editTextMail = (EditText) findViewById(R.id.editTextCorreoRegistro);
         editTextNick = (EditText) findViewById(R.id.editTextNickUser);
         editTextMovil = (EditText) findViewById(R.id.editTextMovil);
         editTextPass = (EditText) findViewById(R.id.editTextPassRegistro);
         editTextConfPass = (EditText) findViewById(R.id.editTextConfPass);


        final Button btnEnviarRegistro = (Button) findViewById(R.id.btnInicioSesionRegistro);
                btnEnviarRegistro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String mail = "";
                        String movil="";
                        String nickUser = "";
                        String contraseña ="";
                        String compruebaContra ="";

                        mail = editTextMail.getText().toString();
                        movil=editTextMovil.getText().toString();
                        nickUser = editTextNick.getText().toString();
                        contraseña=editTextPass.getText().toString();
                        compruebaContra =editTextConfPass.getText().toString();

                       /* try {
                            encriptar(contraseña);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/

                        if (validateRegistro(mail,movil,nickUser,contraseña, compruebaContra))
                            senUserRegistrar(mail,movil,nickUser,contraseña);





                    }
                });


    }

    private void displayResponse(SegUsuario segUsuario) {
        if (segUsuario!=null) {
            Toast.makeText(this, "Usuario " + segUsuario.getUsuario() + " Registrado :)  ", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(RegistraUsuario.this, Denuncias.class);
            intent.putExtra("idUser",segUsuario.getId());
            intent.putExtra("user", segUsuario.getUsuario());
            intent.putExtra("email",segUsuario.getEmail());
            intent.putExtra("movil",segUsuario.getMovil());
            intent.putExtra("clave", segUsuario.getClave());
            startActivity(intent);
        }else {
            Toast.makeText(this, "Correo o nombre de usuario ya existente intente otro  ", Toast.LENGTH_LONG).show();
        }
    }

    public void senUserRegistrar(String mail ,String movil ,String nickUser ,String pass){
        AsyncTask<Object, Object, SegUsuario> httpPostUser = new HttpPostUser(new HttpPostUser.AsynUser() {
            @Override
            public void processUser(SegUsuario segUsuario) {
                displayResponse(segUsuario);
            }
        }).execute(mail,movil,nickUser,pass,this);
    }

    private boolean validateRegistro(String username,String movil,String nickUser ,String password , String confPass){
        if(username == null || username.trim().length() == 0){
            Toast.makeText(this, "Email es requerido", Toast.LENGTH_SHORT).show();
            editTextMail.setText("");
            return false;
        }
        if(movil == null || movil.trim().length() < 10 ){
            Toast.makeText(this, "numero de telefono incorrecto", Toast.LENGTH_SHORT).show();
            editTextMovil.setText("");
            return false;
        }
        if(nickUser == null || nickUser.trim().length() == 0){
            Toast.makeText(this, "nombre de usuario es requerido", Toast.LENGTH_SHORT).show();
            editTextNick.setText("");
            return false;
        }
        if(password == null || password.trim().length() == 0){
            Toast.makeText(this, "Password es requerido", Toast.LENGTH_SHORT).show();
            editTextPass.setText("");
            return false;
        }
        if(confPass == null || confPass.trim().length() == 0){
            Toast.makeText(this, "Confirmar Password es requerido", Toast.LENGTH_SHORT).show();
            editTextConfPass.setText("");
            return false;
        }

        if (!password.equals(confPass)){
                Toast.makeText(this, "las contraseñas no coinciden ", Toast.LENGTH_SHORT).show();
                editTextConfPass.setText("");
                return false;
        }

        if(password.trim().length()<6){
            Toast.makeText(this, "Password debe de tener minimo 6 caracteres", Toast.LENGTH_SHORT).show();
            editTextPass.setText("");
            return false;
        }

        if(!username.contains("@") || (!username.contains(".")) ){
            Toast.makeText(this, "No es un correo valido, revise ", Toast.LENGTH_SHORT).show();
            editTextMail.setText("");
            return false;
        }

        return true;
    }

    public static String encriptar(String mensajeAEncriptar) throws Exception{
        //Instancia del Generador de llaves tipo AES
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        //Inicializamos el generador de llaves especificando el tamaño. Como hemos dicho 128bytes
        keyGenerator.init(128);
        //Instanciamos una llave secreta
        SecretKey secretKey = keyGenerator.generateKey();
        //codificamos la llave en bytes
        byte[] bytesSecretKey = secretKey.getEncoded();
        //Construimos una clave secreta indicandole que es de tipo AES
        SecretKeySpec secretKeySpec = new SecretKeySpec(bytesSecretKey, AES);
        //Instanciamos un objeto de cifrado de tipo AES
        Cipher cipher = Cipher.getInstance(AES);
        //Inicializamos el sistema de cifrado en modo Encriptacion con nuestra clave que hemos creado antes
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        //Procedemos a Encriptar el mensaje
        byte[] mensajeEncritado = cipher.doFinal(mensajeAEncriptar.getBytes());
        Log.d("TAG ENCRIPTADO", new String(mensajeEncritado));

        passwwordEncritado = mensajeEncritado;
        //return new String(mensajeEncritado);

        //Iniciamos el sistema de cifrado en modos Desencriptacion con nuestra clave
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        //Obtenemos el array de bytes del mensaje desencriptado
        byte[] mensajeDesEncriptado = cipher.doFinal(mensajeEncritado);
        Log.d("TAG DESENCRIPTADO", new String(mensajeDesEncriptado));
//
        return new String(mensajeDesEncriptado);

    }

}
