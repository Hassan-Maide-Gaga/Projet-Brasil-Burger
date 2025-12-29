<?php

namespace App\Entity;

use App\Repository\CommandeRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: CommandeRepository::class)]
class Commande
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(name: "date_commande", type: "datetime_immutable")]
    private ?\DateTimeImmutable $dateCommande = null;

    #[ORM\Column(enumType: EtatCommande::class)]
    private ?EtatCommande $etat = null;

    #[ORM\Column(enumType: TypeCommande::class)]
    private ?TypeCommande $type = null;

    #[ORM\Column]
    private ?float $montantTotal = null;

    /**
     * @var Collection<int, CommandeItem>
     */
    #[ORM\OneToMany(targetEntity: CommandeItem::class, mappedBy: 'commande', orphanRemoval: true)]
    private Collection $commandeItems;

    #[ORM\OneToOne(mappedBy: 'commande', targetEntity: Paiement::class)]
    private ?Paiement $paiement = null;

    #[ORM\ManyToOne(targetEntity: User::class)]
    #[ORM\JoinColumn(nullable: false)]
    private ?User $client = null;

    public function __construct()
    {
        $this->commandeItems = new ArrayCollection();
        $this->dateCommande = new \DateTimeImmutable();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getDateCommande(): ?\DateTimeImmutable
    {
        return $this->dateCommande;
    }

    public function setDateCommande(\DateTimeImmutable $dateCommande): static
    {
        $this->dateCommande = $dateCommande;
        return $this;
    }

    public function getEtat(): ?EtatCommande
    {
        return $this->etat;
    }

    public function setEtat(EtatCommande $etat): static
    {
        $this->etat = $etat;
        return $this;
    }

    public function getType(): ?TypeCommande
    {
        return $this->type;
    }

    public function setType(TypeCommande $type): static
    {
        $this->type = $type;
        return $this;
    }

    public function getMontantTotal(): ?float
    {
        return $this->montantTotal;
    }

    public function setMontantTotal(float $montantTotal): static
    {
        $this->montantTotal = $montantTotal;
        return $this;
    }

    /**
     * @return Collection<int, CommandeItem>
     */
    public function getCommandeItems(): Collection
    {
        return $this->commandeItems;
    }

    public function addCommandeItem(CommandeItem $commandeItem): static
    {
        if (!$this->commandeItems->contains($commandeItem)) {
            $this->commandeItems->add($commandeItem);
            $commandeItem->setCommande($this);
        }
        return $this;
    }

    public function removeCommandeItem(CommandeItem $commandeItem): static
    {
        if ($this->commandeItems->removeElement($commandeItem)) {
            if ($commandeItem->getCommande() === $this) {
                $commandeItem->setCommande(null);
            }
        }
        return $this;
    }

    public function getPaiement(): ?Paiement
    {
        return $this->paiement;
    }

    public function setPaiement(Paiement $paiement): static
    {
        $this->paiement = $paiement;
        return $this;
    }

    public function getClient(): ?User
    {
        return $this->client;
    }

    public function setClient(?User $client): static
    {
        $this->client = $client;
        return $this;
    }
}
