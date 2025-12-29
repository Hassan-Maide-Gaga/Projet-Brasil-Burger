<?php

namespace App\Entity;

use App\Repository\PaiementRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: PaiementRepository::class)]
class Paiement
{    
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(name: "created_at", type: "datetime_immutable")]
    private ?\DateTimeImmutable $datePaiement = null;

    #[ORM\Column]
    private ?float $montant = null;

    #[ORM\Column(name: "mode_paiement", enumType: ModePaiement::class)]
    private ?ModePaiement $mode = null;

    #[ORM\Column(length: 100, nullable: true)]
    private ?string $reference = null;

    #[ORM\Column(length: 50)]
    private ?string $statut = 'EN_ATTENTE';

    #[ORM\OneToOne(inversedBy: 'paiement', targetEntity: Commande::class)]
    #[ORM\JoinColumn(name: "commande_id", nullable: false)]
    private ?Commande $commande = null;

    public function __construct()
    {
        $this->datePaiement = new \DateTimeImmutable();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getDatePaiement(): ?\DateTimeImmutable
    {
        return $this->datePaiement;
    }

    public function setDatePaiement(\DateTimeImmutable $datePaiement): static
    {
        $this->datePaiement = $datePaiement;

        return $this;
    }

    public function getMontant(): ?float
    {
        return $this->montant;
    }

    public function setMontant(float $montant): static
    {
        $this->montant = $montant;

        return $this;
    }

    public function getMode(): ?ModePaiement
    {
        return $this->mode;
    }

    public function setMode(ModePaiement $mode): static
    {
        $this->mode = $mode;

        return $this;
    }

    public function getReference(): ?string
    {
        return $this->reference;
    }

    public function setReference(?string $reference): static
    {
        $this->reference = $reference;

        return $this;
    }

    public function getStatut(): ?string
    {
        return $this->statut;
    }

    public function setStatut(string $statut): static
    {
        $this->statut = $statut;

        return $this;
    }

    public function getCommande(): ?Commande
    {
        return $this->commande;
    }

    public function setCommande(?Commande $commande): static
    {
        $this->commande = $commande;

        return $this;
    }
}