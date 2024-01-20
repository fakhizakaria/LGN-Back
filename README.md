# Lingua Nature Backend

Backend de l'application Lingua Nature gérant l'envoi de mails via SendinBlue (Brevo) et la réception de confirmations.

## Fonctionnalités Principales

- **Envoi de Mails:** Utilisation de SendinBlue (Brevo) pour l'envoi de mails.
- **Confirmation de Contact:** Réception de confirmations pour le formulaire "Contact Us".
- **Templates Personnalisés:** Utilisation de templates personnalisés pour les mails avec des données dynamiques.

## Configuration

1. Installez les dépendances : `npm install`

## Utilisation de SendinBlue (Brevo)

1. Obtenez les identifiants API de SendinBlue (Brevo).
2. Configurez ces identifiants dans le fichier de configuration.

## Templates Personnalisés

1. Les templates pour les mails sont situés dans le dossier `templates`.
2. Personnalisez les templates en fonction de vos besoins.

## Endpoints API

### 1. Envoi de Mail

- **Endpoint:** `/contact/submit`
- **Méthode:** `POST`
- **Paramètres Requis:**
  - `name` : nom du sender
  - `email` : email du sender
  - `message` : contenue du mail 

Exemple de requête :
```json
{
  "name": "Sujet du Mail",
  "email": "destinataire@example.com",
  "message": "template_name"
}
