-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : sam. 22 mai 2021 à 12:35
-- Version du serveur :  10.3.16-MariaDB
-- Version de PHP : 7.3.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `id13478871_androidlocation`
--

-- --------------------------------------------------------

--
-- Structure de la table `Annonce`
--

CREATE TABLE `Annonce` (
  `idAnnonce` int(30) NOT NULL,
  `typeBien` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `titre` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `dureedispo` int(11) NOT NULL,
  `Adresse` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `TypeContact` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `ville` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Loyer` int(20) NOT NULL DEFAULT 0,
  `surface` int(20) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `Annonce_immo`
--

CREATE TABLE `Annonce_immo` (
  `idAnnonce` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `typeBien` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `titre` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `dureedispo` int(11) NOT NULL,
  `Adresse` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `TypeContact` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `ville` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `Loyer` int(20) NOT NULL DEFAULT 0,
  `surface` int(20) NOT NULL DEFAULT 0,
  `dateDepot` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `Characteristique`
--

CREATE TABLE `Characteristique` (
  `idChar` int(11) NOT NULL,
  `description` varchar(200) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `char_annonce`
--
-- Erreur de lecture de structure pour la table id13478871_androidlocation.char_annonce : #1932 - Table 'id13478871_androidlocation.char_annonce' doesn't exist in engine

-- --------------------------------------------------------

--
-- Structure de la table `Critere`
--

CREATE TABLE `Critere` (
  `idcritere` int(11) NOT NULL,
  `surface` int(11) NOT NULL,
  `loyer` int(11) NOT NULL,
  `type` varchar(300) COLLATE utf8_unicode_ci NOT NULL,
  `ville` varchar(300) COLLATE utf8_unicode_ci NOT NULL,
  `idutilisateur` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `depot_consulte`
--

CREATE TABLE `depot_consulte` (
  `idutilisateur` int(11) NOT NULL,
  `idAnnonce` varchar(300) COLLATE utf8_unicode_ci NOT NULL,
  `type` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `image`
--

CREATE TABLE `image` (
  `idImage` int(11) NOT NULL,
  `lien` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `idAnnonce` varchar(300) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `Messages`
--

CREATE TABLE `Messages` (
  `idMessage` int(11) NOT NULL,
  `envoyeur` int(11) NOT NULL,
  `destinataire` int(11) NOT NULL,
  `contenu` varchar(10000) COLLATE utf8_unicode_ci NOT NULL,
  `date` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `idUtilisateur` int(11) NOT NULL,
  `password` int(11) NOT NULL,
  `type` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `situationPro` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `tarif` int(11) NOT NULL,
  `nom` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `prenom` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `mail` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `numTel` int(11) NOT NULL,
  `adresse` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `ville` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `quartier` varchar(200) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `utilisateurtoken`
--

CREATE TABLE `utilisateurtoken` (
  `id` int(11) NOT NULL,
  `idutilisateur` int(11) NOT NULL,
  `token` varchar(3000) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `Annonce`
--
ALTER TABLE `Annonce`
  ADD PRIMARY KEY (`idAnnonce`);

--
-- Index pour la table `Annonce_immo`
--
ALTER TABLE `Annonce_immo`
  ADD PRIMARY KEY (`idAnnonce`);

--
-- Index pour la table `Characteristique`
--
ALTER TABLE `Characteristique`
  ADD PRIMARY KEY (`idChar`);

--
-- Index pour la table `Critere`
--
ALTER TABLE `Critere`
  ADD PRIMARY KEY (`idcritere`),
  ADD KEY `fk52` (`idutilisateur`);

--
-- Index pour la table `depot_consulte`
--
ALTER TABLE `depot_consulte`
  ADD KEY `fk4` (`idutilisateur`),
  ADD KEY `fk10` (`idAnnonce`);

--
-- Index pour la table `image`
--
ALTER TABLE `image`
  ADD PRIMARY KEY (`idImage`),
  ADD KEY `fk8` (`idAnnonce`);

--
-- Index pour la table `Messages`
--
ALTER TABLE `Messages`
  ADD PRIMARY KEY (`idMessage`),
  ADD KEY `fk20` (`destinataire`),
  ADD KEY `fk21` (`envoyeur`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`idUtilisateur`);

--
-- Index pour la table `utilisateurtoken`
--
ALTER TABLE `utilisateurtoken`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk36` (`idutilisateur`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `Annonce`
--
ALTER TABLE `Annonce`
  MODIFY `idAnnonce` int(30) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `Characteristique`
--
ALTER TABLE `Characteristique`
  MODIFY `idChar` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `Critere`
--
ALTER TABLE `Critere`
  MODIFY `idcritere` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `image`
--
ALTER TABLE `image`
  MODIFY `idImage` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `Messages`
--
ALTER TABLE `Messages`
  MODIFY `idMessage` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `idUtilisateur` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `utilisateurtoken`
--
ALTER TABLE `utilisateurtoken`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `Critere`
--
ALTER TABLE `Critere`
  ADD CONSTRAINT `fk52` FOREIGN KEY (`idutilisateur`) REFERENCES `utilisateur` (`idUtilisateur`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `depot_consulte`
--
ALTER TABLE `depot_consulte`
  ADD CONSTRAINT `fk10` FOREIGN KEY (`idAnnonce`) REFERENCES `Annonce_immo` (`idAnnonce`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk4` FOREIGN KEY (`idutilisateur`) REFERENCES `utilisateur` (`idUtilisateur`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `image`
--
ALTER TABLE `image`
  ADD CONSTRAINT `fk8` FOREIGN KEY (`idAnnonce`) REFERENCES `Annonce_immo` (`idAnnonce`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `Messages`
--
ALTER TABLE `Messages`
  ADD CONSTRAINT `fk20` FOREIGN KEY (`destinataire`) REFERENCES `utilisateur` (`idUtilisateur`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk21` FOREIGN KEY (`envoyeur`) REFERENCES `utilisateur` (`idUtilisateur`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `utilisateurtoken`
--
ALTER TABLE `utilisateurtoken`
  ADD CONSTRAINT `fk36` FOREIGN KEY (`idutilisateur`) REFERENCES `utilisateur` (`idUtilisateur`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
