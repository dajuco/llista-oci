const express = require("express");
const router = express.Router();

router.get("/", async (req, res) => {
    try {
        const db = req.app.locals.db;

        const llibres = await db
            .collection("llibres")
            .find({}, { projection: { _id: 0 } })
            .toArray();

        res.json(llibres);
    } catch (error) {
        res.status(500).json({
            error: "Error obtenint llibres"
        });
    }
});

router.get("/:id", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const id = req.params.id;

        const llibre = await db
            .collection("llibres")
            .findOne({ id: id }, { projection: { _id: 0 } });

        if (!llibre) {
            return res.status(404).json({
                error: "Llibre no trobat"
            });
        }

        res.json(llibre);
    } catch (error) {
        res.status(500).json({
            error: "Error obtenint el llibre"
        });
    }
});

router.post("/", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const llibre = req.body;

        if (!llibre.id || !llibre.titol || !llibre.autor) {
            return res.status(400).json({
                error: "Falten camps obligatoris"
            });
        }

        const existent = await db.collection("llibres").findOne({ id: llibre.id });

        if (existent) {
            return res.status(409).json({
                error: "Ja existeix un llibre amb aquest id"
            });
        }

        await db.collection("llibres").insertOne({
            id: llibre.id,
            titol: llibre.titol,
            autor: llibre.autor,
            disponible: llibre.disponible ?? true
        });

        res.status(201).json({
            message: "Llibre creat correctament"
        });
    } catch (error) {
        res.status(500).json({
            error: "Error creant el llibre"
        });
    }
});

module.exports = router;