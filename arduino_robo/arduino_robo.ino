// biblioteca para determinar as portas
#include "SoftwareSerial.h"
// biblioteca "cão de guarda"
#include <avr/wdt.h>
SoftwareSerial bluetooth(2, 3); //TX(transmissor), RX(receptor) (Bluetooth)
const int led1 = 4;
const int led2 = 5;
const int led3 = 6;
const int rele1 = 10;
const int rele2 = 11;
const int rele3 = 12;
const int pir1 = 9;
const int pir2 = 8;
const int buz = 7;
String texto1 = "";
String auxiliar = "";
String texto2 = "";
char c;
int segundo = 0;
int minuto = 0;
int i = 0;
int flag = 0;
int flag1 = 0;
unsigned long int tempoAnterior = 0;
unsigned long int tempoAnterior1 = 0;
void setup() {
  // velocidade de transmissão de dados
  bluetooth.begin(9600);
  Serial.setTimeout(50);
  bluetooth.setTimeout(50);
  //wdt_enable(WDTO_2S);
  pinMode(led1, OUTPUT);  pinMode(led2, OUTPUT);  pinMode(led3, OUTPUT);
  pinMode(rele1, OUTPUT);  pinMode(rele2, OUTPUT);  pinMode(rele3, OUTPUT);
  pinMode(buz, OUTPUT);
  pinMode(pir1, INPUT);pinMode(pir2, INPUT);
  digitalWrite(rele1, LOW);digitalWrite(rele2, LOW);digitalWrite(rele3, LOW);digitalWrite(buz, LOW);
  digitalWrite(led2, LOW);
}
void loop() {
  while (bluetooth.available() > 0) {   // Se existem dados para leitura
    wdt_reset();
    c = bluetooth.read();           //Variável para armazenar o dado atual   
    delay(10);
    texto1 += c;                     //Variável para armazenar todos os dados
    if (c == '\n') {                 //Se o dado atual for um pulador de linha (\n)
      flag=0;
      texto1.trim();                 //Remove o \n para comparar o texto1
        if (texto1 == "lig") {        //liga relés.
        ligar();
      }
      if (texto1 == "des") {        //liga relés.
        desligar();
      }
      if (texto1 == "psa") {        //liga relés.
        pausar();
      }
      if (texto1 == "con") {        //liga relés.
        continuar();
      }
      if (texto1 == "15") {        //liga relés.
        minuto = 1;
        flag=0;
      }
      if (texto1 == "30") {        //liga relés.
        minuto = 30;
        flag=0;
      }
      if (texto1 == "60") {        //liga relés.
        minuto = 60;
        flag=0;
      }
      texto1 = "";                   //Limpa o comando para futuras leituras
    }
  }
   if((millis() - tempoAnterior >= 1000) && (flag == 1)) {
    tempoAnterior = millis();
      if(segundo == 0){
        segundo = 60;
        minuto--;
      }
      segundo--;
      if(minuto == 0 && segundo == 0){
        digitalWrite(rele1, LOW);
        digitalWrite(rele2, LOW);
        digitalWrite(rele3, LOW);
        auxiliar = "Concluído";        
     }
   }
  if((millis() - tempoAnterior1 >= 1000) && ((flag == 1)||(flag == 2))){
    tempoAnterior1 = millis();
    enviar();
    if(minuto == 0 && segundo == 0){
      flag = 0;
    }
   }
  if(auxiliar == ("Ligado") && (digitalRead(pir1) || digitalRead(pir2))){
      digitalWrite(rele1, LOW);
      digitalWrite(rele2, LOW);
      digitalWrite(rele3, LOW);
      auxiliar = "Intrusão";
      flag=2;
   }
   if(auxiliar == ("Intrusão") && (!digitalRead(pir1) && !digitalRead(pir2))){
      digitalWrite(rele1, HIGH);
      digitalWrite(rele2, HIGH);
      digitalWrite(rele3, HIGH);
      auxiliar = "Ligado";
      flag=1;
   }
   if(auxiliar == ("Intrusão") && (digitalRead(pir1) || digitalRead(pir2))){
    beep();
   }
}
void ligar(){
  digitalWrite(rele1, HIGH);
  digitalWrite(rele2, HIGH);
  digitalWrite(rele3, HIGH);
  auxiliar = "Ligado";
  flag=1;
}
void desligar(){
  digitalWrite(rele1, LOW);
  digitalWrite(rele2, LOW);
  digitalWrite(rele3, LOW);
        minuto = 0;
        segundo = 0;
        auxiliar = "Desigado";
        flag=2;
}
// tirar
void pausar(){
    if(auxiliar == "Concluído"){
      return;
    }
    digitalWrite(rele1, LOW);
    digitalWrite(rele2, LOW);
    digitalWrite(rele3, LOW);
    auxiliar = "Pausa";
    flag=2;
}
// tirar
void continuar(){
  if(auxiliar == "Concluído"){
    return;
  }
  digitalWrite(rele1, HIGH);
  digitalWrite(rele2, HIGH);
  digitalWrite(rele3, HIGH);
  auxiliar = "Ligado";
  flag=1;
}
void enviar(){
    bluetooth.print(minuto,DEC);
    bluetooth.print("|");
    bluetooth.print(segundo,DEC);
    bluetooth.print("|");
    bluetooth.print(auxiliar);
    bluetooth.print("|");
}
void beep() {
    digitalWrite(buz, HIGH);
    digitalWrite(led1, HIGH);
    delay(200);
    digitalWrite(buz, LOW);
    digitalWrite(led1, LOW);
    delay(100);
}
