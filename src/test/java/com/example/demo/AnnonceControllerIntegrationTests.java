package com.example.demo;

import com.example.demo.entity.Annonce;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnnonceControllerIntegrationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	private Annonce createTestAnnonce(String title) {
		Annonce annonce = new Annonce();
		annonce.setTitle(title);
		annonce.setDescription("Description test");
		annonce.setAdress("Adresse test");
		annonce.setMail("test@example.com");
		annonce.setDate(LocalDateTime.now());
		return annonce;
	}

	// Test de la récupération de toutes les annonces (accessible sans authentification)
	@Test
	public void testGetAnnoncesUnauthenticated() {
		ResponseEntity<Annonce[]> response = restTemplate.getForEntity("/api/annonces", Annonce[].class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	// Test de création d'annonce en tant qu'admin
	@Test
	public void testCreateAnnonceAsAdmin() {
		Annonce annonce = createTestAnnonce("Admin Annonce");
		ResponseEntity<Annonce> response = restTemplate.withBasicAuth("admin", "admin")
				.postForEntity("/api/annonces", annonce, Annonce.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Annonce created = response.getBody();
		assertThat(created).isNotNull();
		assertThat(created.getId()).isNotNull();
		assertThat(created.getTitle()).isEqualTo("Admin Annonce");
	}

	// Test de création d'annonce en tant qu'user
	@Test
	public void testCreateAnnonceAsUser() {
		Annonce annonce = createTestAnnonce("User Annonce");
		ResponseEntity<Annonce> response = restTemplate.withBasicAuth("user", "password")
				.postForEntity("/api/annonces", annonce, Annonce.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Annonce created = response.getBody();
		assertThat(created).isNotNull();
		assertThat(created.getId()).isNotNull();
		assertThat(created.getTitle()).isEqualTo("User Annonce");
	}

	// Test de mise à jour d'une annonce en tant qu'admin
	@Test
	public void testUpdateAnnonceAsAdmin() {
		// Créer une annonce
		Annonce annonce = createTestAnnonce("Annonce à mettre à jour par admin");
		ResponseEntity<Annonce> createResponse = restTemplate.withBasicAuth("admin", "admin")
				.postForEntity("/api/annonces", annonce, Annonce.class);
		Annonce created = createResponse.getBody();
		assertThat(created).isNotNull();

		// Mettre à jour
		created.setTitle("Annonce modifiée par admin");
		HttpEntity<Annonce> requestEntity = new HttpEntity<>(created);
		ResponseEntity<Annonce> updateResponse = restTemplate.withBasicAuth("admin", "admin")
				.exchange("/api/annonces/" + created.getId(), HttpMethod.PUT, requestEntity, Annonce.class);
		assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		Annonce updated = updateResponse.getBody();
		assertThat(updated.getTitle()).isEqualTo("Annonce modifiée par admin");
	}

	// Test de mise à jour d'une annonce en tant qu'user
	@Test
	public void testUpdateAnnonceAsUser() {
		// Créer une annonce
		Annonce annonce = createTestAnnonce("Annonce à mettre à jour par user");
		ResponseEntity<Annonce> createResponse = restTemplate.withBasicAuth("user", "password")
				.postForEntity("/api/annonces", annonce, Annonce.class);
		Annonce created = createResponse.getBody();
		assertThat(created).isNotNull();

		// Mettre à jour
		created.setTitle("Annonce modifiée par user");
		HttpEntity<Annonce> requestEntity = new HttpEntity<>(created);
		ResponseEntity<Annonce> updateResponse = restTemplate.withBasicAuth("user", "password")
				.exchange("/api/annonces/" + created.getId(), HttpMethod.PUT, requestEntity, Annonce.class);
		assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		Annonce updated = updateResponse.getBody();
		assertThat(updated.getTitle()).isEqualTo("Annonce modifiée par user");
	}

	// Test de suppression d'une annonce en tant qu'admin (devrait réussir)
	@Test
	public void testDeleteAnnonceAsAdmin() {
		// Créer une annonce
		Annonce annonce = createTestAnnonce("Annonce à supprimer par admin");
		ResponseEntity<Annonce> createResponse = restTemplate.withBasicAuth("admin", "admin")
				.postForEntity("/api/annonces", annonce, Annonce.class);
		Annonce created = createResponse.getBody();
		assertThat(created).isNotNull();

		// Supprimer en tant qu'admin
		ResponseEntity<Void> deleteResponse = restTemplate.withBasicAuth("admin", "admin")
				.exchange("/api/annonces/" + created.getId(), HttpMethod.DELETE, null, Void.class);
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	// Test de suppression d'une annonce en tant qu'user (devrait échouer avec 403 Forbidden)
	@Test
	public void testDeleteAnnonceAsUserShouldFail() {
		// Créer une annonce en tant qu'admin
		Annonce annonce = createTestAnnonce("Annonce à supprimer par user");
		ResponseEntity<Annonce> createResponse = restTemplate.withBasicAuth("admin", "admin")
				.postForEntity("/api/annonces", annonce, Annonce.class);
		Annonce created = createResponse.getBody();
		assertThat(created).isNotNull();

		// Essayer de supprimer en tant qu'user (ceci doit renvoyer 403 Forbidden)
		ResponseEntity<Void> deleteResponse = restTemplate.withBasicAuth("user", "password")
				.exchange("/api/annonces/" + created.getId(), HttpMethod.DELETE, null, Void.class);
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}
}
