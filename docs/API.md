# Documentation API F1

## 🌐 Jolpica F1 API

Ce widget utilise l'API **Jolpica F1**, qui est le successeur officiel de l'API Ergast.

### Base URL
```
http://api.jolpi.ca/ergast/f1/
```

### Endpoints utilisés

#### 1. Prochain Grand Prix
```http
GET /current/next.json
```

Retourne les informations du prochain Grand Prix à venir.

**Exemple de réponse :**
```json
{
  "MRData": {
    "RaceTable": {
      "Races": [
        {
          "season": "2026",
          "round": "8",
          "raceName": "Monaco Grand Prix",
          "Circuit": {
            "circuitId": "monaco",
            "circuitName": "Circuit de Monaco",
            "Location": {
              "lat": "43.7347",
              "long": "7.42056",
              "locality": "Monte-Carlo",
              "country": "Monaco"
            }
          },
          "date": "2026-05-24",
          "time": "13:00:00Z",
          "FirstPractice": {
            "date": "2026-05-22",
            "time": "11:30:00Z"
          },
          "SecondPractice": {
            "date": "2026-05-22",
            "time": "15:00:00Z"
          },
          "ThirdPractice": {
            "date": "2026-05-23",
            "time": "10:30:00Z"
          },
          "Qualifying": {
            "date": "2026-05-23",
            "time": "14:00:00Z"
          }
        }
      ]
    }
  }
}
```

#### 2. Calendrier complet d'une saison
```http
GET /{year}.json
```

Retourne toutes les courses d'une saison spécifique.

**Exemple :**
```http
GET /2026.json
```

### Champs importants

| Champ | Type | Description |
|-------|------|-------------|
| `season` | string | Année de la saison |
| `round` | string | Numéro du Grand Prix dans la saison |
| `raceName` | string | Nom complet du Grand Prix |
| `Circuit.circuitId` | string | ID unique du circuit (utilisé pour les images) |
| `date` | string | Date de la course (YYYY-MM-DD) |
| `time` | string | Heure de la course (HH:MM:SSZ) |
| `FirstPractice` | object | Détails de la FP1 |
| `SecondPractice` | object | Détails de la FP2 |
| `ThirdPractice` | object | Détails de la FP3 (peut être null) |
| `Qualifying` | object | Détails des qualifications |
| `Sprint` | object | Détails du sprint (peut être null) |

### Circuit IDs

Liste des `circuitId` utilisés par l'API :

```
bahrain, jeddah, albert_park, shanghai, miami, imola, 
monaco, villeneuve, red_bull_ring, silverstone, 
hungaroring, spa, zandvoort, monza, BAK (Baku), 
marina_bay, suzuka, americas, rodriguez, interlagos, 
vegas, losail, yas_marina
```

### Gestion des horaires

- Tous les horaires sont en **UTC** (format ISO 8601)
- Pour afficher en heure locale, le widget convertit automatiquement
- Les week-ends de sprint n'ont pas de FP2 (champ `null`)

### Limites et quotas

✅ **Pas de limite de requêtes**
✅ **Pas d'authentification requise**
✅ **API gratuite et open-source**
⚠️ Respecte un taux raisonnable (max 1 requête/minute)

### Codes d'erreur

| Code | Signification |
|------|---------------|
| 200 | Succès |
| 404 | Ressource non trouvée |
| 500 | Erreur serveur |

## 🔄 Stratégie de mise à jour

Le widget utilise **WorkManager** pour mettre à jour les données :

1. **Toutes les heures** pendant les week-ends de course
2. **Une fois par jour** hors week-end de course
3. **À chaque ajout du widget** sur l'écran

## 🚫 Fallback

En cas d'échec de l'API :
- Le widget garde les dernières données en cache (SharedPreferences)
- Affiche "En attente..." si aucune donnée disponible
- Réessaie automatiquement plus tard

## 🔗 Ressources

- [Documentation officielle Jolpica](http://api.jolpi.ca/ergast/f1/)
- [Ergast API (ancien)](http://ergast.com/mrd/)
- [OpenF1 (alternative)](https://openf1.org/)
