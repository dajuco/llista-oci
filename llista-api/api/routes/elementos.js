const express = require("express");
const router = express.Router();

router.get("/", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const elementos = await db
            .collection("elementos")
            .find({}, { projection: { _id: 0 } })
            .toArray();

        res.json(elementos);
    } catch (error) {
        res.status(500).json({ error: "Error obtenint elements" });
    }
});

router.get("/:id", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const id = req.params.id;

        const elemento = await db
            .collection("elementos")
            .findOne({ id: id }, { projection: { _id: 0 } });

        if (!elemento) return res.status(404).json({ error: "Element no trobat" });

        res.json(elemento);
    } catch (error) {
        res.status(500).json({ error: "Error obtenint element" });
    }
});

router.post("/", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const el = req.body;

        if (!el.id || !el.titulo || !el.descripcion || !el.categoria) {
            return res.status(400).json({ error: "Falten camps obligatoris" });
        }

        const existent = await db.collection("elementos").findOne({ id: el.id });
        if (existent) return res.status(409).json({ error: "Ja existeix un element amb aquest id" });

        await db.collection("elementos").insertOne({
            id: el.id,
            titulo: el.titulo,
            descripcion: el.descripcion,
            categoria: el.categoria
        });

        res.status(201).json({ message: "Element creat correctament" });
    } catch (error) {
        res.status(500).json({ error: "Error creant element" });
    }
});

router.put("/:id", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const id = req.params.id;
        const updates = req.body;

        const result = await db.collection("elementos").updateOne({ id: id }, { $set: updates });

        if (result.matchedCount === 0) return res.status(404).json({ error: "Element no trobat" });

        res.json({ message: "Element actualitzat" });
    } catch (error) {
        res.status(500).json({ error: "Error actualitzant element" });
    }
});

router.delete("/:id", async (req, res) => {
    try {
        const db = req.app.locals.db;
        const id = req.params.id;

        const result = await db.collection("elementos").deleteOne({ id: id });

        if (result.deletedCount === 0) return res.status(404).json({ error: "Element no trobat" });

        res.json({ message: "Element eliminat" });
    } catch (error) {
        res.status(500).json({ error: "Error eliminant element" });
    }
});

module.exports = router;

