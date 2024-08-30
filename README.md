```
    <repositories>
       <repository>
         <id>jitpack.io</id>
         <url>https://jitpack.io</url>
       </repository>
    </repositories>
```

```
     <dependency>
         <groupId>com.github.lunisity1</groupId>
	 <artifactId>NovaItemBuilder</artifactId>
	 <version>1.2</version>
     </dependency>
```





> [!NOTE]
> Only works with Bukkit's Default Configuration System (FileConfiguration)







## Start by going into your config.yml and creating the item, you may follow the steps below:






### Set a title for it, this will just be the path you specify when you need it. I will be calling mine TestItem.

![image](https://github.com/user-attachments/assets/ddb07d58-1581-4b08-8e5d-f452acb7d2b7)


### Next, set a Material for it - make sure to keep the quotation marks around it the Material.

![image](https://github.com/user-attachments/assets/1900df40-e121-4a57-8b6e-04cc9d9f0c8e)


### Next, set the amount you want the ItemStack to be.

![image](https://github.com/user-attachments/assets/6864defa-17ab-44ba-a565-21d5df26207e)


### Next, set the Display Name.

![image](https://github.com/user-attachments/assets/1767e7bb-f9e1-4c7a-9dad-c0f34798aa6f)


### Next, set the Lore - make sure to make the lore a list!

![image](https://github.com/user-attachments/assets/e0c723cd-a5a0-4a95-a957-800321bdb4c8)


### Next, set whether you want the item to glow or not.

![image](https://github.com/user-attachments/assets/75ad39b6-1751-4cee-af1d-925ed0a10243)


### Next, add any item flags you want!

![image](https://github.com/user-attachments/assets/1363a9f4-6efb-4180-a173-6e7ebdb486c5)


### Next, add any enchants you want! Make sure to use the format "ENCHANT;LEVEL" - use the actual Enchantment names!

![image](https://github.com/user-attachments/assets/036e7882-cbd0-4fd4-beca-dce03dea4d6c)


### Next, set whether you want the item to be unbreakable or not.

![image](https://github.com/user-attachments/assets/f47d5292-a960-4ddb-ab7c-350d1a59b76e)


### Next, set what slot you want the item - if no slot is specified, then it will just add it to the player's inventory

![image](https://github.com/user-attachments/assets/7d8071f6-2567-49c5-b5ae-632d8a13d62e)




  
  
  
  ### Congrats, you're done! Now not all of these are required! I will list here what is required and what isn't!

  Path (TestItem) = Required!
  
  Material = Required!
  
  Amount = Not Required!
  
  Name = Not Required!
  
  Lore = Not Required!
  
  Glow = Not Required!
  
  Item-Flags = Not Required!
  
  Enchants = Not Required!
  
  Slot = Not Required!


  ### You can create an item via config with as little code as this:

  ![image](https://github.com/user-attachments/assets/d6ad65ea-35c7-4c04-9330-eb079c3e8602)

  ### That's it!


  ### Now if you want to use colors, there are MANY different options!

  ## Color Formats:

  #### Normal Color Codes = "&"
  
  #### Example: "&cRed!"
  
  #### Hex Color Codes = "&#"
  
  #### Example: "&#880808Red!"
  
  #### Two Color Gradient = <gradient:#color1:#color2>Text</gradient> (You may also add :bold to make it bold! <gradient:#color1:#color2:bold>Text</gradient>)
  
  #### Example: "<gradient:#880808:#DE3163:bold>Gradient from Red to Pink! In Bold!</gradient>
  
  #### Three Color Gradient = <gradient:#color1:#color2:#color3>Text</gradient> (You may also add :bold to make it bold! <gradient:#color1:#color2:#color3:bold>Text</gradient>)
  
  #### Example: "<gradient:#880808:#DE3163:#0096FF:bold>Gradient from Red to Pink to Blue! In Bold!</gradient>




  ## One last thing! In order to get the ItemStack, you would do the following code:


  #### final ItemStack itemStack = ItemBuilder.from(fileConfiguration, path).getItemStack();

  ## You're done! If you have any issues, message @lunisity on Discord!
  
