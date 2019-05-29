This Android app emits sounds of user-specified frequencies

<img src="./huawei_mate_20_screenshot.jpg" width="200" alt="Huawei Mate 20"/>

(Screenshot from Huawei Mate 20)

### Instructions

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/1e1548dad7fc49c3bc8813c19901b10b)](https://app.codacy.com/app/dtczhl/dtc-frequency-player?utm_source=github.com&utm_medium=referral&utm_content=dtczhl/dtc-frequency-player&utm_campaign=Badge_Grade_Dashboard)

There are three ways to specify the frequency

*   type in the frequency
*   use the slide bar
*   automatically change the frequency range

### Source Code
The interface to the phone speaker is the `PlaySound` class. The supported frequency of sounds is from 1 Hz to 24k Hz.

1.  play sound
    ```java
    PlaySound mPlaySound = new PlaySound();
    mPlaySound.mOutputFreq = your_frequency;
    mPlaySound.start();
    ```
    You can change `mPlaySound.mOutputFreq` during the sound playing

2.  stop
    ```java
    if (mPlaySound != null) {
        mPlaySound.stop();
        mPlaySound = null;
    }
    ```

### Phones Tested

*   Huawei Mate 20
*   Google Pixel 2
