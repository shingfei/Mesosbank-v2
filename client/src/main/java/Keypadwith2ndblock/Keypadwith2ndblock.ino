#include <SPI.h>
#include <MFRC522.h>
#include <Keypad.h>

#define RST_PIN         9           // Configurable, see typical pin layout above
#define SS_PIN          10          // Configurable, see typical pin layout above

MFRC522 mfrc522(SS_PIN, RST_PIN);   // Create MFRC522 instance.
MFRC522::MIFARE_Key key;


const byte numRows = 4; //number of rows on the keypad
const byte numCols = 4; //number of columns on the keypad

char keymap[numRows][numCols] =
{
  {'1', '2', '3', 'A'},
  {'4', '5', '6', 'B'},
  {'7', '8', '9', 'C'},
  {'*', '0', '#', 'D'}
};

byte rowPins[numRows] = { 0, 2, 3, 4}; //Rows 0 to 3
byte colPins[numCols]= {5, 6, 7, 8 }; //Columns 0 to 3

Keypad myKeypad = Keypad(makeKeymap(keymap), rowPins, colPins, numRows, numCols);

void setup()
{
  Serial.begin(9600);
  SPI.begin();
  while (!Serial);    // Do nothing if no serial port is opened (added for Arduinos based on ATMEGA32U4)
  intalizeRFID();  
}

void intalizeRFID()
{
    SPI.begin();        // Init SPI bus
    mfrc522.PCD_Init(); // Init MFRC522 card
    for (byte i = 0; i < 6; i++) {
        key.keyByte[i] = 0xFF;
    }
}

void loop()
{
  KeyPad();
  Scanner();  
}

void KeyPad()
{
  char keypressed = myKeypad.getKey();
  if (keypressed != NO_KEY)
  {
    Serial.println(keypressed);
  }
}
 int counter;
void Scanner()
{
 int i;

    // Look for new cards
    if (  mfrc522.PICC_IsNewCardPresent())
    {
  
        // Select one of the cards
        if (  mfrc522.PICC_ReadCardSerial())
        {
            //Serial.println(readBankNr());
            Serial.println(readUID());
      
        }
    }
}

String readUID()
{
  
    return(dump_byte_array(mfrc522.uid.uidByte, mfrc522.uid.size));
}


String readBankNr()
{
   // Show some details of the PICC (that is: the tag/card)
    //Serial.print(F("PICC type: "));
    MFRC522::PICC_Type piccType = mfrc522.PICC_GetType(mfrc522.uid.sak);
    //Serial.println(mfrc522.PICC_GetTypeName(piccType));

    // Check for compatibility
    if (    piccType != MFRC522::PICC_TYPE_MIFARE_MINI
        &&  piccType != MFRC522::PICC_TYPE_MIFARE_1K
        &&  piccType != MFRC522::PICC_TYPE_MIFARE_4K) {
        Serial.println(F("card error"));
        return readBankNr();
    }
    // read block 1(in sector 0 with the keys in of block 3)
    byte sector         = 0;
    byte blockAddr      =1;
    byte trailerBlock   = 3;
    
    MFRC522::StatusCode status;
    byte buffer[18];
    byte size = sizeof(buffer);

    // Authenticate using key A
    status = (MFRC522::StatusCode) mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, trailerBlock, &key, &(mfrc522.uid));
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("PCD_Authenticate() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return readBankNr();
    }
    // Read data from the block
    status = (MFRC522::StatusCode) mfrc522.MIFARE_Read(blockAddr, buffer, &size);
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("MIFARE_Read() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
    }
    String banknr = dump_byte_array(buffer,11);

    // Halt PICC
    mfrc522.PICC_HaltA();
    // Stop encryption on PCD
    mfrc522.PCD_StopCrypto1();
    return banknr;
} 


String dump_byte_array(byte *buffer, byte bufferSize) {
    String output="";
    for (byte i = 0; i < bufferSize; i++) {
        output+=buffer[i] < 0x10 ? " 0" : " ";
        output+=String(buffer[i],HEX);
    }
    return output;
}
