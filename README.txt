        ,'  , `.           ,--,                                 
     ,-+-,.' _ |         ,--.'|                                 
  ,-+-. ;   , ||         |  | :     ,---.    __  ,-.            
 ,--.'|'   |  ;|         :  : '    '   ,'\ ,' ,'/ /|            
|   |  ,', |  ':  ,---.  |  ' |   /   /   |'  | |' | ,--.--.    
|   | /  | |  || /     \ '  | |  .   ; ,. :|  |   ,'/       \   
'   | :  | :  |,/    /  ||  | :  '   | |: :'  :  / .--.  .-. |  
;   . |  ; |--'.    ' / |'  : |__'   | .; :|  | '   \__\/: . .  
|   : |  | ,   '   ;   /||  | '.'|   :    |;  : |   ," .--.; |  
|   : '  |/    '   |  / |;  :    ;\   \  / |  , ;  /  /  ,.  |  
;   | |`-'     |   :    ||  ,   /  `----'   ---'  ;  :   .'   \ 
|   ;/          \   \  /  ---`-'                  |  ,     .-./ 
'---'            `----'                            `--`---'                                

Instrucciones para el manejo y uso de Melora-Microservicios:

| Microservicio       | Puerto | Nombre de la base de datos                         |
|---------------------|--------|----------------------------------------------------|
| register-service    |  8082  | melora_auth                                        |
| recoverPass-service |  8083  | melora_recoverpass                                 |  
| song-service        |  8084  | melora_song                                        |
| playlist-service    |  8085  | melora_playlist                                    |
|                     |        |                                                    |    


Para recoverPass, se implementó un método local en el cuál se envía a través de smtp,
usuario y contraseña de aplicación -> (se necesita autenticación de doble paso para esto)
un correo con la clave necesaria para cambiar la contraseña.
1. Ingresar a https://myaccount.google.com/apppasswords
2. Ingresar nombre de app y crear
3. Copiar contraseña y pegar en spring.mail.password en el properties de recoverpass-service sin espacios, ej: djfe ejdo ejds wngp quedaría así -> djfeejdoejdswngp
4. Ingresar email en spring.mail.username


Aclaraciones importantes respecto a la base de Datos Xampp:
Xampp viene con max_allowed_packet=1M por defecto.Para que acepte las canciones codificadas
hay que cambiarlo en el archivo de configuracion llamado "My.ini" 
Path C:\xampp\mysql\bin\my.ini
Cambiar en el campo nombrado como [mysqld] max_allowed_packet= 512M
