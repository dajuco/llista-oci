const db = db.getSiblingDB("llistaociDB");

db.categorias.insertMany([
    {
        "id": "Pel·lícules",
        "nombre": "Pel·lícules"
    },
    {
        "id": "Videojocs",
        "nombre": "Videojocs"
    },
    {
        "id": "Sèries",
        "nombre": "Sèries"
    },
    {
        "id": "Anime",
        "nombre": "Anime"
    },
    {
        "id": "Música",
        "nombre": "Música"
    },
    {
        "id": "Llibres",
        "nombre": "Llibres"
    },
    {
        "id": "Podcasts",
        "nombre": "Podcasts"
    },
    {
        "id": "Esport",
        "nombre": "Esport"
    },
    {
        "id": "Tecnologia",
        "nombre": "Tecnologia"
    },
    {
        "id": "Viatges",
        "nombre": "Viatges"
    }
]);

db.elementos.insertMany([
    {
        "id": "P-001",
        "titulo": "El Senyor dels Anells",
        "descripcion": "Fantasia èpica",
        "categoria": {
            "id": "Pel·lícules",
            "nombre": "Pel·lícules"
        }
    },
    {
        "id": "J-001",
        "titulo": "The Witcher 3",
        "descripcion": "RPG de món obert",
        "categoria": {
            "id": "Videojocs",
            "nombre": "Videojocs"
        }
    },
    {
        "id": "P-002",
        "titulo": "Interstellar",
        "descripcion": "Ciència ficció espacial",
        "categoria": {
            "id": "Pel·lícules",
            "nombre": "Pel·lícules"
        }
    },
    {
        "id": "P-003",
        "titulo": "Inception",
        "descripcion": "Thriller de somnis",
        "categoria": {
            "id": "Pel·lícules",
            "nombre": "Pel·lícules"
        }
    },
    {
        "id": "P-004",
        "titulo": "Blade Runner 2049",
        "descripcion": "Neo-noir futurista",
        "categoria": {
            "id": "Pel·lícules",
            "nombre": "Pel·lícules"
        }
    },
    {
        "id": "S-001",
        "titulo": "Breaking Bad",
        "descripcion": "Drama criminal",
        "categoria": {
            "id": "Sèries",
            "nombre": "Sèries"
        }
    },
    {
        "id": "S-002",
        "titulo": "Dark",
        "descripcion": "Misteri temporal",
        "categoria": {
            "id": "Sèries",
            "nombre": "Sèries"
        }
    },
    {
        "id": "S-003",
        "titulo": "The Office",
        "descripcion": "Comèdia d'oficina",
        "categoria": {
            "id": "Sèries",
            "nombre": "Sèries"
        }
    },
    {
        "id": "A-001",
        "titulo": "Fullmetal Alchemist: Brotherhood",
        "descripcion": "Aventura i fantasia",
        "categoria": {
            "id": "Anime",
            "nombre": "Anime"
        }
    },
    {
        "id": "A-002",
        "titulo": "Attack on Titan",
        "descripcion": "Acció postapocalíptica",
        "categoria": {
            "id": "Anime",
            "nombre": "Anime"
        }
    },
    {
        "id": "A-003",
        "titulo": "Death Note",
        "descripcion": "Thriller psicològic",
        "categoria": {
            "id": "Anime",
            "nombre": "Anime"
        }
    },
    {
        "id": "J-002",
        "titulo": "Hades",
        "descripcion": "Roguelike d'acció",
        "categoria": {
            "id": "Videojocs",
            "nombre": "Videojocs"
        }
    },
    {
        "id": "J-003",
        "titulo": "Stardew Valley",
        "descripcion": "Simulació relaxada",
        "categoria": {
            "id": "Videojocs",
            "nombre": "Videojocs"
        }
    },
    {
        "id": "J-004",
        "titulo": "Portal 2",
        "descripcion": "Puzles cooperatius",
        "categoria": {
            "id": "Videojocs",
            "nombre": "Videojocs"
        }
    },
    {
        "id": "M-001",
        "titulo": "Random Access Memories",
        "descripcion": "Àlbum de Daft Punk",
        "categoria": {
            "id": "Música",
            "nombre": "Música"
        }
    },
    {
        "id": "M-002",
        "titulo": "Kind of Blue",
        "descripcion": "Clàssic de jazz",
        "categoria": {
            "id": "Música",
            "nombre": "Música"
        }
    },
    {
        "id": "L-001",
        "titulo": "Dune",
        "descripcion": "Novel·la de ciència ficció",
        "categoria": {
            "id": "Llibres",
            "nombre": "Llibres"
        }
    },
    {
        "id": "L-002",
        "titulo": "1984",
        "descripcion": "Distopia clàssica",
        "categoria": {
            "id": "Llibres",
            "nombre": "Llibres"
        }
    },
    {
        "id": "PC-001",
        "titulo": "Lex Fridman Podcast",
        "descripcion": "Converses de tecnologia",
        "categoria": {
            "id": "Podcasts",
            "nombre": "Podcasts"
        }
    },
    {
        "id": "PC-002",
        "titulo": "Hardcore History",
        "descripcion": "História narrativa",
        "categoria": {
            "id": "Podcasts",
            "nombre": "Podcasts"
        }
    },
    {
        "id": "E-001",
        "titulo": "Escalada",
        "descripcion": "Esport de muntanya",
        "categoria": {
            "id": "Esport",
            "nombre": "Esport"
        }
    },
    {
        "id": "E-002",
        "titulo": "Running",
        "descripcion": "Entrenament de resistència",
        "categoria": {
            "id": "Esport",
            "nombre": "Esport"
        }
    },
    {
        "id": "T-001",
        "titulo": "Aprendre Kotlin",
        "descripcion": "Projecte i exercicis de Kotlin",
        "categoria": {
            "id": "Tecnologia",
            "nombre": "Tecnologia"
        }
    },
    {
        "id": "T-002",
        "titulo": "Domòtica amb Home Assistant",
        "descripcion": "Automatització de la llar",
        "categoria": {
            "id": "Tecnologia",
            "nombre": "Tecnologia"
        }
    },
    {
        "id": "V-001",
        "titulo": "Ruta per Islàndia",
        "descripcion": "Itinerari de 7 dies",
        "categoria": {
            "id": "Viatges",
            "nombre": "Viatges"
        }
    },
    {
        "id": "V-002",
        "titulo": "Cap de setmana a Lisboa",
        "descripcion": "Guia ràpida urbana",
        "categoria": {
            "id": "Viatges",
            "nombre": "Viatges"
        }
    }
]);

db.usuarios.insertMany([
    {
        "type": "models.UserSuperAdmin",
        "username": "super",
        "password": "1234",
        "display": "Super Usuari"
    },
    {
        "type": "models.UserAdmin",
        "username": "admin",
        "password": "1234",
        "display": "Administrador"
    },
    {
        "type": "models.UserNormal",
        "username": "anna",
        "password": "1234",
        "display": "Anna",
        "elementsUser": [
            {
                "elementOciId": "P-001",
                "estado": "EN_PROCÉS"
            },
            {
                "elementOciId": "J-003",
                "estado": "PENDENT"
            },
            {
                "elementOciId": "L-001",
                "estado": "COMPLETAT"
            }
        ]
    },
    {
        "type": "models.UserNormal",
        "username": "marc",
        "password": "1234",
        "display": "Marc",
        "elementsUser": [
            {
                "elementOciId": "S-002",
                "estado": "PENDENT"
            },
            {
                "elementOciId": "A-001",
                "estado": "EN_PROCÉS"
            },
            {
                "elementOciId": "V-002",
                "estado": "PENDENT"
            }
        ]
    },
    {
        "type": "models.UserNormal",
        "username": "laia",
        "password": "1234",
        "display": "Laia",
        "elementsUser": [
            {
                "elementOciId": "M-001",
                "estado": "PENDENT"
            },
            {
                "elementOciId": "PC-001",
                "estado": "PENDENT"
            },
            {
                "elementOciId": "V-001",
                "estado": "COMPLETAT"
            }
        ]
    },
    {
        "type": "models.UserNormal",
        "username": "joan",
        "password": "1234",
        "display": "Joan",
        "elementsUser": [
            {
                "elementOciId": "J-001",
                "estado": "COMPLETAT"
            },
            {
                "elementOciId": "P-004",
                "estado": "EN_PROCÉS"
            },
            {
                "elementOciId": "E-002",
                "estado": "PENDENT"
            }
        ]
    },
    {
        "type": "models.UserNormal",
        "username": "marta",
        "password": "1234",
        "display": "Marta",
        "elementsUser": [
            {
                "elementOciId": "S-001",
                "estado": "PENDENT"
            }
        ]
    },
    {
        "type": "models.UserNormal",
        "username": "nil",
        "password": "1234",
        "display": "Nil"
    },
    {
        "type": "models.UserNormal",
        "username": "test",
        "password": "1234",
        "display": "test"
    },
    {
        "type": "models.UserNormal",
        "username": "Gerard",
        "password": "EwnizEv5",
        "display": "Gerard P."
    }
]);
