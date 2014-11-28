
## Overview

Engocab helps to learn words of language you are studying.

Each word can have several translations. For the translation you can add description, which shows difficulties of using a word. 
Examples are added to the translation to show context of how the word is used.


## Model

Main classes:


* **WordKey** 
> contains **word**, **part of speech**, **number**
>
> **number** allows to distinguish different words with the same spelling

* **Dictionary** 
> maps **WordKey**s to **WordContainer**s
>
> **WordContainer** allows to store several prioritized translations for a **WordKey**

* **WordRecord**
> contains translation for the **WordKey**
>
> **WordContainer** stores **WordRecords**. 
>
> contains examples of how a **WordKey** with the give translation is used
>
> contains list of tags

* **Example**
> **WordRecord** contains **Example**s of how a **WordKey** is used

* **Synonym**
> **WordRecord** contains **Synonym**s of a **WordKey**

