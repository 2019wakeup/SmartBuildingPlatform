<template>
  <div class="welcome-section">
    <div class="welcome-content">
      <h1 class="welcome-title">Welcome</h1>
    </div>
    
    <!-- College aerial view showcase -->
    <div class="college-showcase">
      <div class="college-image-container">
        <img src="@/JEI.jpg" alt="Manchester Metropolitan Joint Institute, Hubei University" class="college-image" />
        <div class="college-overlay">
          <h3>Manchester Metropolitan Joint Institute</h3>
          <p>Hubei University</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// No props needed anymore
</script>

<style scoped>
.welcome-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 40px;
  color: white;
  margin-bottom: 30px;
}
.welcome-content {
  text-align: center;
  margin-bottom: 40px;
}
.welcome-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 10px;
}

/* College aerial view styles */
.college-showcase {
  margin-top: 30px;
}
.college-image-container {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
}
.college-image {
  width: 100%;
  height: 300px;
  object-fit: cover;
  display: block;
}
.college-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.8));
  padding: 30px 20px 20px;
  text-align: center;
}
.college-overlay h3 {
  margin: 0 0 8px 0;
  font-size: 1.5rem;
  font-weight: 600;
  color: white;
}
.college-overlay p {
  margin: 0;
  font-size: 1rem;
  color: rgba(255, 255, 255, 0.9);
}
</style>
