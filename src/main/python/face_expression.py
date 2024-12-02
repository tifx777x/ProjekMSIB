#!/usr/bin/env python
# coding: utf-8

# In[12]:


import cv2
import numpy as np
import pandas as pd
from tensorflow.keras.models import load_model

# Muat model deteksi ekspresi wajah
model = load_model('emotion_model.h5')  # Pastikan Anda memiliki model deteksi ekspresi wajah

# Muat dataset movies.csv untuk rekomendasi
movies_df = pd.read_csv('emotion_service/dataset/processed_movies.csv')

# Daftar ekspresi yang dapat dikenali
emotion_labels = ['Angry', 'Disguist', 'Fear', 'Happy', 'Sad', 'Surprise', 'Neutral']

# Muat model deteksi wajah
face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')

# Fungsi untuk merekomendasikan film berdasarkan ekspresi
def recommend_movies(emotion):
    recommendations = {
        'Happy': ['Comedy', 'Romantic', 'Action'],
        'Sad': ['Comedy','Drama', 'Romantic'],
        'Angry': ['Action', 'Thriller','Comedy'],
        'Fear': ['Horror', 'Mystery'],
        'Surprise': ['Fantasy', 'Action','Thriller'],
        'Neutral': ['Adventure', 'Animation'],
        'Disguist': ['Horror', 'Thriller']
    }

    genres = recommendations.get(emotion, ['General'])
    recommended_movies = movies_df[movies_df['genres'].isin(genres)]
    return recommended_movies[['title', 'genres']]

# Fungsi untuk mendeteksi wajah dan ekspresi
def detect_emotion_from_face():
    cap = cv2.VideoCapture(0)
    while True:
        ret, frame = cap.read()
        if not ret:
            break

        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        faces = face_cascade.detectMultiScale(gray, 1.1, 4)

        for (x, y, w, h) in faces:
            face = frame[y:y + h, x:x + w]
            # Mengubah wajah menjadi RGB dengan tiga saluran warna
            face = cv2.cvtColor(face, cv2.COLOR_BGR2RGB)
            
            # Pastikan dimensi gambar sesuai dengan yang diinginkan model (misalnya 48x48x3)
            face = cv2.resize(face, (48, 48))
            
            # Normalisasi gambar ke rentang [0, 1]
            face = face / 255.0
            
            # Menambahkan dimensi untuk batch dan channel terakhir (48, 48, 3)
            face = np.expand_dims(face, axis=0)
            
            # Prediksi ekspresi wajah
            emotion_prob = model.predict(face)
            max_index = np.argmax(emotion_prob[0])
            emotion = emotion_labels[max_index]


            # Menampilkan ekspresi wajah dan rekomendasi film
            recommended_movies = recommend_movies(emotion)
            print(f"Detected Emotion: {emotion}")
            print("Recommended Movies:")
            print(recommended_movies)

        cap.release()

# Menjalankan fungsi deteksi ekspresi wajah
if __name__ == "__main__":
    detect_emotion_from_face()


# In[ ]:




