# Guide des Images de Circuits F1

## 🗃️ Images nécessaires

Pour que le widget affiche correctement les tracés des circuits, tu dois ajouter les images suivantes dans le dossier `app/src/main/res/drawable/` :

### Liste complète des circuits 2026

1. `circuit_bahrain.png` - Bahrain International Circuit
2. `circuit_jeddah.png` - Jeddah Corniche Circuit
3. `circuit_albert_park.png` - Albert Park Circuit (Australie)
4. `circuit_shanghai.png` - Shanghai International Circuit
5. `circuit_miami.png` - Miami International Autodrome
6. `circuit_imola.png` - Autodromo Enzo e Dino Ferrari
7. `circuit_monaco.png` - Circuit de Monaco
8. `circuit_villeneuve.png` - Circuit Gilles Villeneuve (Canada)
9. `circuit_red_bull_ring.png` - Red Bull Ring (Autriche)
10. `circuit_silverstone.png` - Silverstone Circuit
11. `circuit_hungaroring.png` - Hungaroring
12. `circuit_spa.png` - Circuit de Spa-Francorchamps
13. `circuit_zandvoort.png` - Circuit Zandvoort
14. `circuit_monza.png` - Autodromo Nazionale di Monza
15. `circuit_baku.png` - Baku City Circuit
16. `circuit_marina_bay.png` - Marina Bay Street Circuit (Singapour)
17. `circuit_suzuka.png` - Suzuka International Racing Course
18. `circuit_americas.png` - Circuit of the Americas (Austin)
19. `circuit_rodriguez.png` - Autódromo Hermanos Rodríguez (Mexico)
20. `circuit_interlagos.png` - Autódromo José Carlos Pace (Brésil)
21. `circuit_vegas.png` - Las Vegas Street Circuit
22. `circuit_losail.png` - Losail International Circuit (Qatar)
23. `circuit_yas_marina.png` - Yas Marina Circuit (Abu Dhabi)
24. `circuit_default.png` - Image par défaut (fallback)

## 💾 Où trouver les images

### Option 1 : Wikipedia Commons (Recommandée) ✅

Les images de haute qualité sont disponibles sur Wikipedia Commons :

1. Va sur https://commons.wikimedia.org/wiki/Category:Formula_One_circuit_maps
2. Cherche le circuit spécifique
3. Télécharge l'image SVG ou PNG
4. Si c'est un SVG, convertis-le en PNG

### Option 2 : Sites spécialisés

- **F1 Sketch** : https://f1sketch.com (tracés minimalistes)
- **RacingCircuits.info** : Base de données complète
- **Official F1 Media** : Images officielles (vérifie la licence)

## 🎨 Spécifications des images

### Format requis
- **Format** : PNG avec transparence
- **Dimensions recommandées** : 800x600px
- **Fond** : Transparent ou noir (#000000)
- **Tracé** : Blanc (#FFFFFF) ou couleur claire
- **Style** : Minimaliste, lignes claires

### Traitement des images

#### Avec GIMP (gratuit)
```bash
1. Ouvre l'image dans GIMP
2. Image > Échelle et taille de l'image > 800x600px
3. Couleurs > Luminosité-Contraste (ajuste pour fond noir)
4. Calque > Transparence > Ajouter un canal alpha
5. Sélection > Par couleur > Sélectionne le fond blanc
6. Édition > Effacer (pour rendre transparent)
7. Fichier > Exporter comme > PNG
```

#### Avec ImageMagick (ligne de commande)
```bash
# Redimensionner
convert circuit.png -resize 800x600 circuit_resized.png

# Inverser les couleurs (blanc sur noir)
convert circuit.png -negate circuit_inverted.png

# Rendre le fond transparent
convert circuit.png -transparent white circuit_transparent.png
```

## 📝 Nommage des fichiers

Le widget utilise le `circuitId` de l'API pour charger les images. Voici le mapping :

| Circuit ID (API) | Nom du fichier |
|------------------|----------------|
| `bahrain` | `circuit_bahrain.png` |
| `jeddah` | `circuit_jeddah.png` |
| `albert_park` | `circuit_albert_park.png` |
| `monaco` | `circuit_monaco.png` |
| `silverstone` | `circuit_silverstone.png` |
| `monza` | `circuit_monza.png` |
| `spa` | `circuit_spa.png` |
| `suzuka` | `circuit_suzuka.png` |
| ... | ... |

⚠️ **Important** : Le nom du fichier doit être exactement `circuit_` + `circuitId` + `.png`

## 🔧 Installation rapide

1. Crée le dossier si nécessaire :
```bash
mkdir -p app/src/main/res/drawable
```

2. Place toutes tes images PNG dans ce dossier

3. Vérifie que les noms correspondent au mapping ci-dessus

4. Rebuild le projet dans Android Studio

## 🎯 Image par défaut

Crée une image `circuit_default.png` qui sera affichée si le circuit spécifique n'est pas trouvé.

Suggestions pour l'image par défaut :
- Logo F1 simple
- Icône de drapeau à damier
- Texte "Circuit non disponible"
- Silhouette générique de circuit

## 🔍 Vérification

Après avoir ajouté les images, vérifie dans Android Studio :

1. Ouvre `Build > Make Project`
2. Si erreur : vérifie les noms de fichiers
3. Les fichiers doivent apparaître dans `R.drawable.circuit_xxx`

## ⚙️ Personnalisation

Si tu veux modifier la logique de mapping, édite la méthode `getCircuitDrawable()` dans `F1Widget.kt` :

```kotlin
private fun getCircuitDrawable(context: Context, circuitId: String): Int {
    val resourceId = context.resources.getIdentifier(
        "circuit_$circuitId",
        "drawable",
        context.packageName
    )
    
    return if (resourceId != 0) resourceId 
    else R.drawable.circuit_default
}
```

## 📊 Taille des fichiers

- Garde chaque PNG sous 100 KB pour optimiser la taille de l'APK
- Utilise la compression PNG (pngquant, TinyPNG)
- Total pour 24 circuits : ~2 MB maximum

## 🔗 Ressources utiles

- [Wikipedia F1 Circuits](https://commons.wikimedia.org/wiki/Category:Formula_One_circuit_maps)
- [F1 Sketch](https://f1sketch.com/)
- [GIMP](https://www.gimp.org/) - Éditeur d'images gratuit
- [ImageMagick](https://imagemagick.org/) - Outil CLI
- [TinyPNG](https://tinypng.com/) - Compression PNG
